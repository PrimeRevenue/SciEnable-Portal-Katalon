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

WebUI.setText(findTestObject('Page_Portal Web/input_Job Title'), 'QA')

// Both email and phone invalid -> button should stay disabled
WebUI.setText(findTestObject('Page_Portal Web/input_Email'), 'notanemail')

WebUI.setText(findTestObject('Page_Portal Web/input_Phone Number'), 'abc!!!')

WebUI.verifyElementNotClickable(findTestObject('Page_Portal Web/button_Next'))

WebUI.sendKeys(findTestObject('Page_Portal Web/input_Email'), Keys.chord(Keys.CONTROL, 'a'))

WebUI.sendKeys(findTestObject('Page_Portal Web/input_Email'), Keys.chord(Keys.DELETE))

// Fix email only -> still disabled (phone still invalid)
WebUI.setText(findTestObject('Page_Portal Web/input_Email'), 'smoketest@gmail.com')

WebUI.verifyElementNotClickable(findTestObject('Page_Portal Web/button_Next'))

WebUI.sendKeys(findTestObject('Page_Portal Web/input_Phone Number'), Keys.chord(Keys.CONTROL, 'a'))

WebUI.sendKeys(findTestObject('Page_Portal Web/input_Phone Number'), Keys.chord(Keys.DELETE))

// Fix phone too -> now enabled
WebUI.setText(findTestObject('Page_Portal Web/input_Phone Number'), '1234567898')

// Form validates on blur — confirmed live 9/22/2026: the same field values only flip the button
// enabled once focus leaves the Phone field. setText alone doesn't blur it, so tab out first.
WebUI.sendKeys(findTestObject('Page_Portal Web/input_Phone Number'), Keys.chord(Keys.TAB))

WebUI.verifyElementClickable(findTestObject('Page_Portal Web/button_Next'))

