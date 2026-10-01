import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.llm.keyword.LlmKeywords as LLM
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser(null)

// Step 1: React portal URL, flagged-off supplier — shell chrome (app-switch tabs) must NOT appear
WebUI.navigateToUrl(GlobalVariable.reactPortalBaseUrl)

WebUI.setText(findTestObject('Portal Shell/input_LoginId'), GlobalVariable.ffOffSupplierEmail)

// ffSupplierPassword switched from protected/encrypted to plain 9/29/2026 — setEncryptedText()
// couldn't decode the stored value, and plain setText was typing literal ciphertext before that.
// Plain setText avoids the encrypt/decrypt round-trip for this shared internal test account.
WebUI.setText(findTestObject('Portal Shell/input_Password'), GlobalVariable.ffSupplierPassword)

WebUI.click(findTestObject('Portal Shell/button_Submit'))

WebUI.delay(3)

// This is the PT-266 bug's exact repro path (supplier + FF off couldn't log in at all, saw repeated
// 2FA/login loop). That bug is marked resolved, but re-assert it here since it's the most likely
// regression if flag-off handling breaks again.
WebUI.verifyTextNotPresent('Invalid login credentials', false)

// Confirmed live 10/1/2026 with supplier.scienable: login lands on SCiSupplier's Switch Company
// page served directly (regression.primerevenue.com/scf/accountSwitcher.htm) — no shell, so no
// iframe. Everything below is in the top-level document: no switchToFrame, and none of the
// iframe-bound Page_Portal Web objects (their ref_element would make Katalon look for an iframe
// that doesn't exist here). This account has a different, much longer company list than the
// flag-on account, hence its own data file.
TestData companies = findTestData('Data Files/FlagOffSupplierCompanies')

// Remember the switcher's URL to return to between companies. A header "Switch Company" link
// can't be relied on: selecting a company can land on a "complete your user profile" page
// (confirmed live 10/1/2026 for '324') that has no header menu at all.
TestObject accountSwitcherForm = new TestObject('accountSwitcherForm')
accountSwitcherForm.addProperty('xpath', ConditionType.EQUALS, "//form[@id='accountSwitcher']")
WebUI.verifyElementPresent(accountSwitcherForm, 20)
String switcherUrl = WebUI.getUrl()

for (int i = 1; i <= companies.getRowNumbers(); i++) {
    String companyName = companies.getValue('companyName', i)

    if (i > 1) {
        WebUI.navigateToUrl(switcherUrl)
    }

    TestObject companyRow = new TestObject('companyRow_' + companyName)
    // normalize-space(.) not text() — the company name is nested inside child elements of the <li>.
    companyRow.addProperty('xpath', ConditionType.EQUALS, "//form[@id='accountSwitcher']/div/ul/li[normalize-space(.)='" + companyName + "']")
    WebUI.click(companyRow)

    // Wait until we've actually left the switcher (wherever the company lands — home or the
    // profile-completion page) so the shell check below runs against the company's page.
    WebUI.verifyElementNotPresent(accountSwitcherForm, 20)

    WebUI.verifyElementNotPresent(findTestObject('Portal Shell/button_SwitchToSCiEnable'), 10)
}

WebUI.closeBrowser()

// Step 2: SCiEnable direct URL still works, undisturbed by the shell/flag
WebUI.openBrowser(null)

WebUI.navigateToUrl(GlobalVariable.oldUiBaseUrl)

WebUI.verifyElementPresent(findTestObject('Old Registration/input_CompanyLegalName'), 10)

WebUI.closeBrowser()
