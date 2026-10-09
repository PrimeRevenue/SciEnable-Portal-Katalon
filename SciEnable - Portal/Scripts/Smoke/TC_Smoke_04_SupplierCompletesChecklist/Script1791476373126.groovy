import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// SE-15103: supplier completes Company Details, Platform Administrator, Financial Decision Maker
// and My Preferences (payment preferences). Each item's form loads in its own iframe; the recorded
// objects mixed up frames (e.g. the TPA "Yes" reused for FDM), so frames are switched explicitly here.
// trim(): values pasted from Gmail can carry a hidden tab/line break.
String email = (GlobalVariable.smokeSupplierEmail ?: supplierEmail)?.trim()
if (!email?.startsWith('supplier.scienable+smoke')) {
    KeywordUtil.markFailedAndStop('No smoke supplier email — run via the smoke suite, or set the supplierEmail variable')
}

TestObject byXpath(String name, String xpath) {
    TestObject to = new TestObject(name)
    to.addProperty('xpath', ConditionType.EQUALS, xpath)
    return to
}

String cls(String c) {
    return "contains(concat(' ', normalize-space(@class), ' '), ' " + c + " ')"
}

// Opens a checklist item from the left menu (outer page), then switches into its iframe.
void openItem(String hrefPart, String frameXpath) {
    WebUI.switchToDefaultContent()
    WebUI.click(byXpath('menu_' + hrefPart, "//a[contains(@href, '/" + hrefPart + "')]//*[" + cls('user_side_page_link') + "]"))
    WebUI.switchToFrame(byXpath('frame_' + hrefPart, frameXpath), 20)
}

// After saving, Company Details / Platform Administrator / FDM show a "Completed" label in their frame
// (no popup); My Preferences shows a "Success" notification instead (confirmed live 10/9).
void verifyCompletedLabel() {
    WebUI.verifyElementPresent(byXpath('completedLabel', "//*[normalize-space(text())='Completed']"), 20)
}

WebUI.openBrowser(null)

WebUI.navigateToUrl('https://scienable-reg.aws.primerevenue.com')

WebUI.setText(findTestObject('Page_Regression - SCiEnable/input_Email'), email)

WebUI.setText(findTestObject('Page_Regression - SCiEnable/input_Password'), GlobalVariable.smokeSupplierPassword)

WebUI.click(findTestObject('Page_Regression - SCiEnable/button_Submit'))

// 1. Company Details: a two-page form inside company-detail-frame. Page 1 is an info box with a
// "Next" button (confirmed 10/9); page 2 has State/Province of Incorporation + tax ID and a primary
// button that saves. The frame's content loads after the frame itself, so wait for "Next" to show.
openItem('company_info_detailed', "//iframe[@id='company-detail-frame']")
TestObject next = byXpath('companyNext', "//*[" + cls('labeled') + " and normalize-space(.)='Next']")
WebUI.waitForElementVisible(next, 20)
WebUI.click(next)
WebUI.click(byXpath('stateDropdown', "//*[@id='company-details-incorporation-state-select']//i[" + cls('dropdown') + "]"))
WebUI.click(byXpath('firstState', "(//*[@id='company-details-incorporation-state-select']//*[" + cls('item') + "])[1]"))
WebUI.setText(byXpath('taxId', "//input[@name='taxId']"), '123456789')

// Page 2's save button is labelled "Save" (confirmed 10/9).
WebUI.click(byXpath('companySave', "//button[" + cls('primary') + " and normalize-space(.)='Save']"))
verifyCompletedLabel()

// 2. Platform Administrator: "Are you the Platform Administrator?" -> Yes -> Save (details prefilled)
openItem('supplier_sa_creation', "//iframe[@id='tpa-frame']")
WebUI.click(byXpath('tpaYes', "//*[" + cls('is-admin-buttons') + "]//*[" + cls('ant-btn-primary') + "]"))
WebUI.click(byXpath('tpaSave', "//*[" + cls('ant-btn-primary') + " and " + cls('btn-submit') + "]"))
verifyCompletedLabel()

// 3. Financial Decision Maker: Yes -> Save (details prefilled)
openItem('supplier_fdm_creation', "//iframe[@id='fdm-frame']")
WebUI.click(byXpath('fdmYes', "//*[" + cls('is-admin-buttons') + "]//*[" + cls('ant-btn-primary') + "]"))
WebUI.click(byXpath('fdmSave', "//*[" + cls('ant-btn-primary') + " and " + cls('btn-submit') + "]"))
verifyCompletedLabel()

// 4. My Preferences (payment preferences): acknowledge checkbox -> Save
openItem('trading_preference', "//iframe[@scrolling='no']")
WebUI.click(byXpath('acknowledge', "//input[" + cls('ant-checkbox-input') + "]"))
WebUI.click(byXpath('preferencesSave', "//*[" + cls('ant-btn') + " and normalize-space(.)='Save']"))
WebUI.verifyElementPresent(byXpath('successNotice', "//*[" + cls('ant-notification-notice-message') + " and normalize-space(.)='Success']"), 20)

// SE-15103: "On Refresh, [item] should move from Not Started to Completed". The left menu lists items
// under a "Not Started" heading, then a "Completed" heading (confirmed 10/9).
WebUI.switchToDefaultContent()
WebUI.refresh()
WebUI.waitForElementVisible(byXpath('menuCompany', "//a[contains(@href, '/company_info_detailed')]"), 20)
String menu = WebUI.executeJavaScript("return document.body.innerText.replace(/\\s+/g, ' ')", null)
int notStartedAt = menu.indexOf(' Not Started ')
int completedAt = menu.indexOf(' Completed ')
if (completedAt < 0) {
    KeywordUtil.markFailedAndStop('Onboarding checklist has no "Completed" section after saving')
}
// Items still outstanding sit between the two headings (the section is absent once all are done).
String notStarted = (notStartedAt >= 0 && notStartedAt < completedAt) ? menu.substring(notStartedAt, completedAt) : ''
for (String item : ['Company Details', 'Platform Administrator', 'Financial Decision Maker', 'My Preferences']) {
    if (notStarted.contains(item) || menu.indexOf(item, completedAt) < 0) {
        KeywordUtil.markFailed(item + ' did not move from Not Started to Completed in the onboarding checklist')
    }
}

WebUI.closeBrowser()
