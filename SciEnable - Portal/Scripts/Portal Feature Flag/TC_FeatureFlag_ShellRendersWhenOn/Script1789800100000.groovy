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

WebUI.navigateToUrl(GlobalVariable.reactPortalBaseUrl)

WebUI.setText(findTestObject('Portal Shell/input_LoginId'), GlobalVariable.ffOnSupplierEmail)

// ffSupplierPassword switched from protected/encrypted to plain 9/29/2026 — setEncryptedText()
// couldn't decode the stored value, and plain setText was typing literal ciphertext before that.
// Plain setText avoids the encrypt/decrypt round-trip for this shared internal test account.
WebUI.setText(findTestObject('Portal Shell/input_Password'), GlobalVariable.ffSupplierPassword)

WebUI.click(findTestObject('Portal Shell/button_Submit'))

WebUI.delay(3)

WebUI.verifyTextNotPresent('Invalid login credentials', false)

// Confirmed live 10/1/2026: now that MFA is off, login actually completes and lands on a "Switch
// Company" account switcher inside an iframe — a step every prior run was blocked from ever
// reaching. This account has multiple companies; looping through all of them (from the
// SupplierCompanies data file) and confirming the shell renders for each one, per Danniecia's
// request to verify this works across every company rather than picking just one.
TestData companies = findTestData('Data Files/SupplierCompanies')

for (int i = 1; i <= companies.getRowNumbers(); i++) {
    String companyName = companies.getValue('companyName', i)

    WebUI.switchToFrame(findTestObject('Page_Portal Web/iframe_SCiSupplier_MuiBox-root css-akvbpd'), 10)

    TestObject companyRow = new TestObject('companyRow_' + companyName)

    // Uses normalize-space(.) not normalize-space(text()) — the company name sits nested inside
    // child elements, not as a direct text node of the <li>, so text() alone matched nothing
    // (failed live 10/1/2026). "." captures the full string-value including descendants.
    companyRow.addProperty('xpath', ConditionType.EQUALS, ('//form[@id=\'accountSwitcher\']/div/ul/li[normalize-space(.)=\'' +
        companyName) + '\']')

    WebUI.click(companyRow)

    // Selecting a company auto-navigates (confirmed 10/1/2026) — no separate confirm button.
    WebUI.switchToDefaultContent()

    // SCiSupplier home content renders either wrapped in the shell or served directly — the
    // app-switch chrome is what actually distinguishes "shell rendered" from a same-looking
    // direct/fallback page.
    WebUI.verifyElementPresent(findTestObject('Portal Shell/button_SwitchToSCiEnable'), 10)

    // "Recent Activity" lives inside the SciSupplier iframe, not the outer shell document — text
    // checks after switchToDefaultContent() searched the wrong document. Objects whose ref_element
    // points at the iframe are switched into it by Katalon automatically, so don't switchToFrame
    // manually here (doing both makes Katalon look for the iframe from inside itself and fail).
    // verifyElementPresent, not waitForElementPresent — the wait keyword only warns on timeout.
    WebUI.verifyElementPresent(findTestObject('Page_Portal Web/div_Recent Activity'), 20)

    // Back to the company switcher for the next company, unless this was the last one. Uses the
    // in-app "Switch Company" link (hover the header's current-company dropdown, click the
    // revealed link) instead of WebUI.back(). Both objects are positional, so they work
    // regardless of which company is active, and both carry the iframe ref_element (see above).
    if (i < companies.getRowNumbers()) {
        WebUI.mouseOver(findTestObject('Page_Portal Web/a_rest api'))

        WebUI.waitForElementVisible(findTestObject('Page_Portal Web/a_Switch Company'), 10)

        WebUI.click(findTestObject('Page_Portal Web/a_Switch Company'))

        WebUI.delay(2)
    }
}

WebUI.closeBrowser()
