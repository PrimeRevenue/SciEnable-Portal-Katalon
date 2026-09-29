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

WebUI.setText(findTestObject('Page_Portal Web/input_Email'), 'smoketest+' + System.currentTimeMillis() + '@gmail.com')

WebUI.setText(findTestObject('Page_Portal Web/input_Job Title'), 'QA')

WebUI.setText(findTestObject('Page_Portal Web/input_Phone Number'), '1234567898')

WebUI.click(findTestObject('Page_Portal Web/button_Next'))

WebUI.setText(findTestObject('Page_Portal Web/input_Enter company Tax ID'), '123456789')

WebUI.click(findTestObject('Page_Portal Web/button_Search'))

// Retested 9/24 — the 9/17 "no enforcement" finding was stale. New UI now
// rejects an oversized value with an inline error, same as it does for Address 1's PO Box rule.
WebUI.setText(findTestObject('Page_Portal Web/input_Enter postal code'), '123456789012345678901234567890123456789012')

WebUI.sendKeys(findTestObject('Page_Portal Web/input_Enter postal code'), Keys.chord(Keys.TAB))

WebUI.verifyTextPresent('Please enter a valid postal code', false)

WebUI.closeBrowser()
