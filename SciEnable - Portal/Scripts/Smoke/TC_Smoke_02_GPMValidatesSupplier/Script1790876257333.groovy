import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// SE-15100: GPM validates the supplier registered by TC_Smoke_01 (run them together in TS_Smoke).
String companyName = GlobalVariable.smokeCompanyName
if (!companyName?.startsWith('Smoke Supplier ')) {
    KeywordUtil.markFailedAndStop('smokeCompanyName is "' + companyName + '" — run this via TS_Smoke so TC_Smoke_01 registers a supplier first')
}

TestObject byXpath(String name, String xpath) {
    TestObject to = new TestObject(name)
    to.addProperty('xpath', ConditionType.EQUALS, xpath)
    return to
}

WebUI.openBrowser(null)

// Opening the auth-reg authorize URL directly (as recorded) doesn't show the login form; the app
// root redirects to it properly.
WebUI.navigateToUrl('https://scienable-reg.aws.primerevenue.com')

WebUI.setText(findTestObject('Page_Regression - SCiEnable/input_Email'), GlobalVariable.smokeGpmEmail)

// setText, not setEncryptedText: a protected profile variable already arrives here as plain text
// (Katalon 11 decrypts it from its secure store). setEncryptedText would base64-decode it again —
// "Illegal base64 character 21" when the password contains '!' (failed live 10/6/2026).
WebUI.setText(findTestObject('Page_Regression - SCiEnable/input_Password'), GlobalVariable.smokeGpmPassword)

WebUI.click(findTestObject('Page_Regression - SCiEnable/button_Submit'))

// Program group, found by its name (the recorded locator depended on its position on the page).
WebUI.click(byXpath('programOutlook', "//*[contains(concat(' ', normalize-space(@class), ' '), ' user_side_page_link ') and normalize-space(.)='outlook program']"))

WebUI.click(findTestObject('Page_SE/span_Registered'))

// Search for this run's supplier and pick it from the autocomplete list by its exact name
// (the recorded list item's id, ui-id-NN, changes every time).
WebUI.setText(findTestObject('Page_SE/input_Search Suppliers'), companyName)

TestObject suggestion = byXpath('supplierSuggestion', "//li[contains(concat(' ', normalize-space(@class), ' '), ' ui-menu-item ') and normalize-space(.)='" + companyName + "']")
WebUI.mouseOver(suggestion)
WebUI.click(suggestion)

WebUI.click(findTestObject('Page_SE/img_search_supplier_icon'))

// After the search, the Registered list shows only this supplier; open its validation page.
WebUI.click(findTestObject('Page_SE/a_gpm_list_with_hover'))

WebUI.click(byXpath('validateButton', "//input[@value='Validate']"))

// Post-condition per SE-15100: success message for this supplier.
WebUI.verifyElementPresent(byXpath('validatedMessage', "//span[contains(concat(' ', normalize-space(@class), ' '), ' message ') and normalize-space(.)='Supplier " + companyName + " validated successfully.']"), 20)

WebUI.closeBrowser()
