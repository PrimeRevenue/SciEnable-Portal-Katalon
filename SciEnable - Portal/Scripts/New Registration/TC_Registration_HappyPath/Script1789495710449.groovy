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

WebUI.setText(findTestObject('Page_Portal Web/input_Fax Number'), '1234567')

WebUI.click(findTestObject('Page_Portal Web/button_Next'))

WebUI.setText(findTestObject('Page_Portal Web/input_Enter company Tax ID'), '123456789')

WebUI.click(findTestObject('Page_Portal Web/button_Search'))

WebUI.setText(findTestObject('Page_Portal Web/input_Enter your company name'), 'Test')

WebUI.setText(findTestObject('Page_Portal Web/input_Enter your street address'), '123 Test Drive')

WebUI.setText(findTestObject('Page_Portal Web/input_Enter postal code'), '34567')

WebUI.click(findTestObject('Page_Portal Web/input_Select country'))

WebUI.mouseOver(findTestObject('Page_Portal Web/li_registration-countryId-option-0'))

WebUI.click(findTestObject('Page_Portal Web/li_registration-countryId-option-0'))

WebUI.waitForElementNotPresent(findTestObject('Page_Portal Web/ul_countryId_listbox'), 5)

WebUI.click(findTestObject('Page_Portal Web/svg_MuiSvgIcon-root MuiSvgIcon-fontSizeMedium cs'))

WebUI.mouseOver(findTestObject('Page_Portal Web/li_registration-stateId-option-0'))

WebUI.click(findTestObject('Page_Portal Web/li_registration-stateId-option-0'))

WebUI.waitForElementNotPresent(findTestObject('Page_Portal Web/ul_stateId_listbox'), 5)

WebUI.setText(findTestObject('Page_Portal Web/input_Enter city'), 'Atlanta')

WebUI.click(findTestObject('Page_Portal Web/div_Tax ID_Company Name_Address_ZIP_Postal Co'))

WebUI.click(findTestObject('Page_Portal Web/svg_MuiSvgIcon-root MuiSvgIcon-fontSizeMedium cs_1'))

WebUI.mouseOver(findTestObject('Page_Portal Web/li_registration-organizationCountryId-option-0'))

WebUI.click(findTestObject('Page_Portal Web/li_registration-organizationCountryId-option-0'))

WebUI.waitForElementNotPresent(findTestObject('Page_Portal Web/ul_organizationCountryId_listbox'), 5)

WebUI.click(findTestObject('Page_Portal Web/svg_MuiSvgIcon-root MuiSvgIcon-fontSizeMedium cs_2'))

WebUI.mouseOver(findTestObject('Page_Portal Web/li_registration-organizationStateId-option-0'))

WebUI.click(findTestObject('Page_Portal Web/li_registration-organizationStateId-option-0'))

WebUI.waitForElementNotPresent(findTestObject('Page_Portal Web/ul_organizationStateId_listbox'), 5)

WebUI.click(findTestObject('Page_Portal Web/svg_MuiSvgIcon-root MuiSvgIcon-fontSizeMedium cs_3'))

WebUI.mouseOver(findTestObject('Page_Portal Web/li_registration-organizationTypeId-option-0'))

WebUI.click(findTestObject('Page_Portal Web/li_registration-organizationTypeId-option-0'))

WebUI.waitForElementNotPresent(findTestObject('Page_Portal Web/ul_organizationTypeId_listbox'), 5)

WebUI.click(findTestObject('Page_Portal Web/div_Tax ID_Company Name_Address_ZIP_Postal Co'))

WebUI.waitForElementNotPresent(findTestObject('Page_Portal Web/ul_organizationTypeId_listbox'), 5)

WebUI.click(findTestObject('Page_Portal Web/svg_MuiSvgIcon-root MuiSvgIcon-fontSizeMedium cs_4'))

WebUI.mouseOver(findTestObject('Page_Portal Web/li_registration-question-11-option-0'))

WebUI.click(findTestObject('Page_Portal Web/li_registration-question-11-option-0'))

WebUI.click(findTestObject('Page_Portal Web/span_Early Pay Rampsite'))

WebUI.click(findTestObject('Page_Portal Web/span_Dynamic Discount Rampsite'))

WebUI.click(findTestObject('Page_Portal Web/span_Reverse Factoring Rampsite'))

WebUI.click(findTestObject('Page_Portal Web/div_Tax ID_Company Name_Address_ZIP_Postal Co'))

WebUI.click(findTestObject('Page_Portal Web/button_Submit'))

WebUI.verifyElementPresent(findTestObject('Page_Portal Web/text_RegistrationSubmitted'), 10)

