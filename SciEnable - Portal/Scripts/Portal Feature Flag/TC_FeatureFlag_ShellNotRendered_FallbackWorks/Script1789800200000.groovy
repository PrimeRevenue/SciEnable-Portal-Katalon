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

// Step 1: React portal URL, flagged-off supplier — shell chrome (app-switch tabs) must NOT appear
WebUI.navigateToUrl(GlobalVariable.reactPortalBaseUrl)

WebUI.setText(findTestObject('Portal Shell/input_LoginId'), GlobalVariable.ffOffSupplierEmail)

WebUI.setText(findTestObject('Portal Shell/input_Password'), GlobalVariable.ffSupplierPassword)

WebUI.click(findTestObject('Portal Shell/button_Submit'))

WebUI.delay(3)

// This is the PT-266 bug's exact repro path (supplier + FF off couldn't log in at all, saw repeated
// 2FA/login loop). That bug is marked resolved, but re-assert it here since it's the most likely
// regression if flag-off handling breaks again.
WebUI.verifyTextNotPresent('Invalid login credentials', false)

WebUI.verifyElementNotPresent(findTestObject('Portal Shell/button_SwitchToSCiEnable'), 10)

WebUI.closeBrowser()

// Step 2: SCiEnable direct URL still works, undisturbed by the shell/flag
WebUI.openBrowser(null)

WebUI.navigateToUrl(GlobalVariable.oldUiBaseUrl)

WebUI.verifyElementPresent(findTestObject('Old Registration/input_CompanyLegalName'), 10)

WebUI.closeBrowser()
