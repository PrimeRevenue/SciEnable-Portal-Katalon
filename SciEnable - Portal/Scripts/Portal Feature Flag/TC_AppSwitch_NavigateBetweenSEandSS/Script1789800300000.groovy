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

WebUI.navigateToUrl(GlobalVariable.reactPortalBaseUrl)

WebUI.setText(findTestObject('Portal Shell/input_LoginId'), GlobalVariable.ffOnSupplierEmail)

WebUI.setText(findTestObject('Portal Shell/input_Password'), GlobalVariable.ffSupplierPassword)

WebUI.click(findTestObject('Portal Shell/button_Submit'))

WebUI.delay(3)

// Default landing app — SCiSupplier home content per PT-268 attachment
WebUI.verifyTextPresent('Recent Activity', false)

// Switch SS -> SE (~2-3s per PT-272)
WebUI.click(findTestObject('Portal Shell/button_SwitchToSCiEnable'))

WebUI.delay(4)

// SCiEnable onboarding dashboard content per PT-268 attachment
WebUI.verifyTextPresent('Onboarding Checklist', false)

// No re-authentication should be required on switch (FR-7 / US-01)
WebUI.verifyTextNotPresent('Invalid login credentials', false)

// Switch SE -> SS (~2s per PT-272)
WebUI.click(findTestObject('Portal Shell/button_SwitchToSCiSupplier'))

WebUI.delay(3)

WebUI.verifyTextPresent('Recent Activity', false)

WebUI.closeBrowser()
