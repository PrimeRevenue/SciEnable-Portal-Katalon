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

WebUI.navigateToUrl(GlobalVariable.oldUiBaseUrl)

WebUI.setText(findTestObject('Old Registration/input_CompanyLegalName'), 'Supplier Smoke')

WebUI.setText(findTestObject('Old Registration/input_Address1'), 'G-203, Navyug Infosolutions, Noida')

WebUI.setText(findTestObject('Old Registration/input_City'), 'Noida')

WebUI.selectOptionByLabel(findTestObject('Old Registration/select_Country'), 'India', false)

WebUI.setText(findTestObject('Old Registration/input_PostalCode'), '201307')

WebUI.selectOptionByLabel(findTestObject('Old Registration/select_OrgCountry'), 'India', false)

WebUI.delay(1)

WebUI.selectOptionByLabel(findTestObject('Old Registration/select_OrgType'), 'Corporation', false)

WebUI.selectOptionByLabel(findTestObject('Old Registration/select_Currency'), 'US Dollar', false)

WebUI.selectOptionByLabel(findTestObject('Old Registration/select_DesiredProgram'), 'OutlookBuyer', false)

WebUI.setText(findTestObject('Old Registration/input_Question1'), 'Test')

WebUI.setText(findTestObject('Old Registration/input_FirstName'), 'Smoke')

WebUI.setText(findTestObject('Old Registration/input_LastName'), 'Test')

WebUI.setText(findTestObject('Old Registration/input_JobTitle'), 'QA')

WebUI.setText(findTestObject('Old Registration/input_Email'), 'smoketest@gmail.com')

WebUI.setText(findTestObject('Old Registration/input_Phone'), '1234567898')

WebUI.setText(findTestObject('Old Registration/input_Fax'), '1234567')

WebUI.click(findTestObject('Old Registration/button_Submit'))
