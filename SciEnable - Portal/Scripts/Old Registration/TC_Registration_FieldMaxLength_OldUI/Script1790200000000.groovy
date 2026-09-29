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

// Max length is enforced via HTML maxlength attribute, not a triggered error —
// same pattern already used for Address 1 (50) and Postal Code (15).
WebUI.verifyElementAttributeValue(findTestObject('Old Registration/input_CompanyLegalName'), 'maxlength', '100', 10)

WebUI.verifyElementAttributeValue(findTestObject('Old Registration/input_City'), 'maxlength', '50', 10)

WebUI.verifyElementAttributeValue(findTestObject('Old Registration/input_FirstName'), 'maxlength', '50', 10)

WebUI.verifyElementAttributeValue(findTestObject('Old Registration/input_LastName'), 'maxlength', '50', 10)

WebUI.verifyElementAttributeValue(findTestObject('Old Registration/input_Email'), 'maxlength', '50', 10)

WebUI.closeBrowser()
