package smoke

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/** Shared steps for the supplier side of the smoke flow (SCiEnable onboarding, Regression). */
class Onboarding {

	static TestObject byXpath(String name, String xpath) {
		TestObject to = new TestObject(name)
		to.addProperty('xpath', ConditionType.EQUALS, xpath)
		return to
	}

	static String cls(String c) {
		return "contains(concat(' ', normalize-space(@class), ' '), ' " + c + " ')"
	}

	/**
	 * The supplier to act as: step 1's smokeSupplierEmail in a suite run, otherwise the test's own
	 * supplierEmail variable. trim() because values pasted from Gmail can carry a hidden tab/line break.
	 */
	@Keyword
	static String supplierEmail(String fallback) {
		String email = (GlobalVariable.smokeSupplierEmail ?: fallback)?.trim()
		if (!email?.startsWith('supplier.scienable+smoke')) {
			KeywordUtil.markFailedAndStop('No smoke supplier email — run via the smoke suite, or set the supplierEmail variable')
		}
		return email
	}

	/** Opens a browser and logs in to SCiEnable as the given smoke supplier. */
	@Keyword
	static void loginSupplier(String email) {
		WebUI.openBrowser(null)
		WebUI.navigateToUrl('https://scienable-reg.aws.primerevenue.com')
		WebUI.setText(findTestObject('Page_Regression - SCiEnable/input_Email'), email)
		// setText, not setEncryptedText: protected variables arrive as plain text at runtime.
		WebUI.setText(findTestObject('Page_Regression - SCiEnable/input_Password'), GlobalVariable.smokeSupplierPassword)
		Date submittedAt = new Date()
		WebUI.click(findTestObject('Page_Regression - SCiEnable/button_Submit'))
		// Smoke suppliers' +alias addresses all deliver to the GMAILUSERNAME inbox.
		passTwoFactorIfAsked(email, GlobalVariable.GMAILUSERNAME, GlobalVariable.GMAILAPPPASSWORD, submittedAt)
	}

	/**
	 * If the login asks for a two-factor code (MFA turned on for the account), reads the code from
	 * the account's Gmail and enters it; otherwise does nothing. The page (seen 10/8): "Two-factor
	 * Code" box, "Trust this computer for 30 days" (useless here, every run uses a fresh browser), Verify.
	 */
	static void passTwoFactorIfAsked(String accountEmail, String gmailUser, String gmailAppPassword, Date submittedAt) {
		TestObject codeInput = byXpath('twoFactorCode', "//input[@placeholder='Two-factor Code']")
		if (!WebUI.waitForElementVisible(codeInput, 10, FailureHandling.OPTIONAL)) {
			return
		}
		if (!gmailAppPassword) {
			KeywordUtil.markFailedAndStop("${accountEmail} asked for a two-factor code, but there's no Gmail app password to read it with — set it in the Regression profile")
		}
		// Only emails received after this login's Submit (5s grace for clock differences), so a code
		// from an earlier step's login a few seconds before can't be picked up.
		String code = GmailReader.waitForValue(gmailUser, gmailAppPassword, accountEmail, TWO_FACTOR_SUBJECT,
				TWO_FACTOR_REGEX, submittedAt, 180, 5)
		WebUI.setText(codeInput, code)
		WebUI.click(byXpath('twoFactorVerify', "//button[normalize-space(.)='Verify']"))
		if (!WebUI.waitForElementNotPresent(codeInput, 15, FailureHandling.OPTIONAL)) {
			KeywordUtil.markFailedAndStop("Two-factor code for ${accountEmail} was not accepted")
		}
	}

	// TODO confirm from a real code email: subject text and the code's format (assumed 6 digits).
	static final String TWO_FACTOR_SUBJECT = ''
	static final String TWO_FACTOR_REGEX = '(?<![0-9])([0-9]{6})(?![0-9])'

	/**
	 * The supplier company to act on: step 1's smokeCompanyName in a suite run, otherwise the test's
	 * own companyName variable.
	 */
	@Keyword
	static String companyName(String fallback) {
		String name = (GlobalVariable.smokeCompanyName ?: fallback)?.trim()
		if (!name?.startsWith('Smoke Supplier ')) {
			KeywordUtil.markFailedAndStop('No smoke supplier company name — run via the smoke suite, or set the companyName variable')
		}
		return name
	}

	/** Opens a browser and logs in to SCiEnable as the GPM (gpm.scienable). */
	@Keyword
	static void loginGpm() {
		WebUI.openBrowser(null)
		WebUI.navigateToUrl('https://scienable-reg.aws.primerevenue.com')
		WebUI.setText(findTestObject('Page_Regression - SCiEnable/input_Email'), GlobalVariable.smokeGpmEmail)
		WebUI.setText(findTestObject('Page_Regression - SCiEnable/input_Password'), GlobalVariable.smokeGpmPassword)
		Date submittedAt = new Date()
		WebUI.click(findTestObject('Page_Regression - SCiEnable/button_Submit'))
		// gpm.scienable is itself a Gmail address, with its own app password.
		passTwoFactorIfAsked(GlobalVariable.smokeGpmEmail, GlobalVariable.smokeGpmEmail, GlobalVariable.GPMGMAILAPPPASSWORD, submittedAt)
	}

	/**
	 * As GPM: opens outlook program, searches the supplier list (the tab currently shown) for the
	 * exact company name and opens that supplier's dashboard.
	 */
	@Keyword
	static void openSupplierAsGpm(String companyName) {
		// Locators are built here rather than taken from the Object Repository: recordings re-save
		// shared objects (10/14 the step 9 recording overwrote step 2's supplier link).
		WebUI.click(byXpath('programOutlook', "//*[" + cls('user_side_page_link') + " and normalize-space(.)='outlook program']"))
		WebUI.setText(byXpath('searchSuppliers', "//input[@id='search_supplier']"), companyName)
		TestObject suggestion = byXpath('supplierSuggestion', "//li[" + cls('ui-menu-item') + " and normalize-space(.)='" + companyName + "']")
		WebUI.mouseOver(suggestion)
		WebUI.click(suggestion)
		WebUI.click(byXpath('searchSuppliersIcon', "//*[@id='search_supplier_icon']"))
		// The supplier's dashboard link sits in the same list entry as its name.
		WebUI.click(byXpath('supplierDashboardLink', "(//*[normalize-space(text())='" + companyName + "']/ancestor::*[.//*[" + cls('gpm_list_with_hover') + "]][1]//*[" + cls('gpm_list_with_hover') + "])[1]"))
	}

	/** Clicks an onboarding checklist item in the left menu, matched by part of its link. */
	@Keyword
	static void openChecklistItem(String hrefPart) {
		WebUI.switchToDefaultContent()
		WebUI.click(byXpath('menu_' + hrefPart, "//a[contains(@href, '/" + hrefPart + "')]//*[" + cls('user_side_page_link') + "]"))
	}

	/**
	 * Reloads the page and checks each item has moved from "Not Started" to "Completed" in the left
	 * checklist menu. The menu lists outstanding items under "Not Started", then done items under
	 * "Completed" (confirmed 10/9).
	 */
	@Keyword
	static void verifyCompleted(List<String> items) {
		WebUI.switchToDefaultContent()
		WebUI.refresh()
		WebUI.waitForElementVisible(byXpath('checklistMenu', "//*[" + cls('user_side_page_link') + "]"), 20)
		String menu = WebUI.executeJavaScript("return document.body.innerText.replace(/\\s+/g, ' ')", null)
		int notStartedAt = menu.indexOf(' Not Started ')
		int completedAt = menu.indexOf(' Completed ')
		if (completedAt < 0) {
			KeywordUtil.markFailedAndStop('Onboarding checklist has no "Completed" section')
		}
		String notStarted = (notStartedAt >= 0 && notStartedAt < completedAt) ? menu.substring(notStartedAt, completedAt) : ''
		for (String item : items) {
			if (notStarted.contains(item) || menu.indexOf(item, completedAt) < 0) {
				KeywordUtil.markFailed(item + ' did not move from Not Started to Completed in the onboarding checklist')
			}
		}
	}
}
