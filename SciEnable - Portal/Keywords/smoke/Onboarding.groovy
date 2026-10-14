package smoke

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
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
		WebUI.click(findTestObject('Page_Regression - SCiEnable/button_Submit'))
	}

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
		WebUI.click(findTestObject('Page_Regression - SCiEnable/button_Submit'))
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
