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

WebUI.setText(findTestObject('Page_Portal Web/input_First Name'), 'Smoke')

WebUI.setText(findTestObject('Page_Portal Web/input_Last Name'), 'Test')

WebUI.setText(findTestObject('Page_Portal Web/input_Email'), 'smoketest@gmail.com')

WebUI.setText(findTestObject('Page_Portal Web/input_Job Title'), 'QA')

WebUI.setText(findTestObject('Page_Portal Web/input_Phone Number'), '1234567898')

WebUI.click(findTestObject('Page_Portal Web/button_Next'))

WebUI.setText(findTestObject('Page_Portal Web/input_Enter company Tax ID'), '123456789')

WebUI.click(findTestObject('Page_Portal Web/button_Search'))

WebUI.setText(findTestObject('Page_Portal Web/input_Enter your street address'), 'PO Box 123')

WebUI.setText(findTestObject('Page_Portal Web/input_Enter your company name'), 'Test')

WebUI.verifyTextPresent('P.O. Box addresses are not allowed', false)

// RETESTED 9/24/2026: the 9/18 "no disallowed-character validation" finding was stale — likely
// never blurred the field before checking. With a real blur (Keys.chord(Keys.TAB)) after typing,
// aria-invalid correctly flips to "true" for "123 Main St!". New UI does validate this field.
WebUI.setText(findTestObject('Page_Portal Web/input_Enter your street address'), '123 Main St!')

WebUI.sendKeys(findTestObject('Page_Portal Web/input_Enter your street address'), Keys.chord(Keys.TAB))

WebUI.verifyElementAttributeValue(findTestObject('Page_Portal Web/input_Enter your street address'), 'aria-invalid', 'true', 10)

WebUI.closeBrowser()
