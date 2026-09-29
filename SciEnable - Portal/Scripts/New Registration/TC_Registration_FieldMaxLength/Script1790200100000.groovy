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
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser(null)

WebUI.navigateToUrl(GlobalVariable.baseUrl)

// First Name / Last Name / Email / Job Title — confirmed 9/24 to have no max-length cap at all.
// Type well past any reasonable limit and confirm the full string survives untruncated.
def oversizedName = 'A' * 80

WebUI.setText(findTestObject('Page_Portal Web/input_First Name'), oversizedName)
assert WebUI.getAttribute(findTestObject('Page_Portal Web/input_First Name'), 'value').length() == oversizedName.length() : 'First Name should have no max-length cap (known gap, confirmed 9/24)'

WebUI.setText(findTestObject('Page_Portal Web/input_Last Name'), oversizedName)
assert WebUI.getAttribute(findTestObject('Page_Portal Web/input_Last Name'), 'value').length() == oversizedName.length() : 'Last Name should have no max-length cap (known gap, confirmed 9/24)'

def oversizedEmail = ('A' * 80) + '@gmail.com'

WebUI.setText(findTestObject('Page_Portal Web/input_Email'), oversizedEmail)
assert WebUI.getAttribute(findTestObject('Page_Portal Web/input_Email'), 'value').length() == oversizedEmail.length() : 'Email should have no max-length cap (known gap, confirmed 9/24)'

WebUI.setText(findTestObject('Page_Portal Web/input_Job Title'), oversizedName)
assert WebUI.getAttribute(findTestObject('Page_Portal Web/input_Job Title'), 'value').length() == oversizedName.length() : 'Job Title should have no max-length cap (observed 9/24, not a regression since Old UI has no documented cap either)'

WebUI.setText(findTestObject('Page_Portal Web/input_Phone Number'), '1234567898')

WebUI.click(findTestObject('Page_Portal Web/button_Next'))

WebUI.setText(findTestObject('Page_Portal Web/input_Enter company Tax ID'), '123456789')

WebUI.click(findTestObject('Page_Portal Web/button_Search'))

// Company Name — confirmed live 9/29 via screenshot: the field does NOT truncate at all. It accepts
// the full oversized value and shows a validation error instead — "Company name must be 200
// characters or fewer" — same mechanism as Postal Code's "Please enter a valid postal code".
// The field's value legitimately stays at the full typed length; only the error text confirms the cap.
def oversizedCompanyName = 'A' * 220

WebUI.click(findTestObject('Page_Portal Web/input_Enter your company name'))
WebUI.sendKeys(findTestObject('Page_Portal Web/input_Enter your company name'), oversizedCompanyName)
WebUI.sendKeys(findTestObject('Page_Portal Web/input_Enter your company name'), Keys.chord(Keys.TAB))
WebUI.verifyTextPresent('Company name must be 200 characters or fewer', false)

// City — assumed to follow the same error-message pattern as Company Name (not yet confirmed live —
// exact wording is a guess pending your next run; tell me the real text if this fails so I can fix it).
def oversizedCity = 'A' * 120

WebUI.click(findTestObject('Page_Portal Web/input_Enter city'))
WebUI.sendKeys(findTestObject('Page_Portal Web/input_Enter city'), oversizedCity)
WebUI.sendKeys(findTestObject('Page_Portal Web/input_Enter city'), Keys.chord(Keys.TAB))
WebUI.verifyTextPresent('City must be 100 characters or fewer', false)

WebUI.closeBrowser()
