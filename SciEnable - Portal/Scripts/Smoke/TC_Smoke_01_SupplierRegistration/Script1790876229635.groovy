import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

// Unique per run: the GPM picks the supplier by company name in SE-15100, and a reused name
// ("Supplier Smoke") would match every earlier run's registration too.
String runId = new Date().format('MMddHHmmss')
GlobalVariable.smokeCompanyName = 'Smoke Supplier ' + runId
GlobalVariable.smokeSupplierEmail = 'supplier.scienable+smoke' + runId + '@gmail.com'
KeywordUtil.logInfo('Smoke supplier: ' + GlobalVariable.smokeCompanyName + ' / ' + GlobalVariable.smokeSupplierEmail)

WebUI.openBrowser(null)

WebUI.navigateToUrl(GlobalVariable.oldUiBaseUrl)

WebUI.setText(findTestObject('Old Registration/input_CompanyLegalName'), GlobalVariable.smokeCompanyName)

WebUI.setText(findTestObject('Old Registration/input_Address1'), 'G-203, Navyug Infosolutions, Noida')

WebUI.setText(findTestObject('Old Registration/input_City'), 'Noida')

WebUI.selectOptionByLabel(findTestObject('Old Registration/select_Country'), 'India', false)

WebUI.setText(findTestObject('Old Registration/input_PostalCode'), '201307')

WebUI.selectOptionByLabel(findTestObject('Old Registration/select_OrgCountry'), 'India', false)

WebUI.delay(2)

// SE-15096 lists "Corporation", which no longer exists for India on Regression.
WebUI.selectOptionByLabel(findTestObject('Old Registration/select_OrgType'), 'General Partnership', false)

WebUI.selectOptionByLabel(findTestObject('Old Registration/select_Currency'), 'US Dollar', false)

WebUI.click(findTestObject('Old Registration/input_DesiredProgramSearch'))

WebUI.click(findTestObject('Old Registration/li_DesiredProgram_OutlookBuyer'))

WebUI.setText(findTestObject('Old Registration/input_Question1'), 'Test')

WebUI.setText(findTestObject('Old Registration/input_FirstName'), 'Smoke')

WebUI.setText(findTestObject('Old Registration/input_LastName'), 'Test')

WebUI.setText(findTestObject('Old Registration/input_JobTitle'), 'QA')

WebUI.setText(findTestObject('Old Registration/input_Email'), GlobalVariable.smokeSupplierEmail)

WebUI.setText(findTestObject('Old Registration/input_Phone'), '1234567898')

WebUI.setText(findTestObject('Old Registration/input_Fax'), '1234567')

WebUI.click(findTestObject('Old Registration/button_Submit'))

WebUI.verifyElementPresent(findTestObject('Old Registration/text_RegistrationConfirmation'), 10)

WebUI.closeBrowser()
