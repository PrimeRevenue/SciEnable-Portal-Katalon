import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import smoke.Onboarding

// SE-6150: GPM approves every required document the supplier uploaded in step 8.
String company = Onboarding.companyName(companyName)

// Required Documents opens inside the dashboard's inner frame. Each document row shows a status
// label: "In Review" (yellow) before approval, "Ready to Send to Funder" (blue) after (recorded 10/14).
TestObject itemFrame = Onboarding.byXpath('dashboardFrame', "//iframe[@scrolling='no']")
TestObject inReview = Onboarding.byXpath('inReviewDoc', "//*[" + Onboarding.cls('yellow') + " and normalize-space(.)='In Review']")
TestObject approve = Onboarding.byXpath('approve', "//*[" + Onboarding.cls('primary') + " and normalize-space(.)='Approve']")
TestObject back = Onboarding.byXpath('backToDocs', "//*[" + Onboarding.cls('button') + " and normalize-space(.)='Back To Required Documents']")
TestObject readyToSend = Onboarding.byXpath('readyToSend', "//*[" + Onboarding.cls('blue') + " and normalize-space(.)='Ready to Send to Funder']")

Onboarding.loginGpm()
Onboarding.openSupplierAsGpm(company)

// The dashboard may open on Required Documents already (it did when recorded); click it if offered.
// GPM menu links have ids page_link_<n> (the number changes; self-healing found #page_link_220 on 10/14).
WebUI.click(Onboarding.byXpath('requiredDocsMenu', "//*[starts-with(@id, 'page_link_') and normalize-space(.)='Required Documents']"), FailureHandling.OPTIONAL)
WebUI.switchToFrame(itemFrame, 20)

int approved = 0
for (int i = 0; i < 10 && WebUI.waitForElementVisible(inReview, 10, FailureHandling.OPTIONAL); i++) {
    WebUI.click(inReview)
    WebUI.click(approve)
    WebUI.click(back)
    approved++
    // Right after "Back" the old page (with the just-approved document still "In Review") lingers for a
    // moment; checking it then clicks a label that vanishes mid-click (10/14 suite run). Wait for the
    // list to reload — it shows at least one "Ready to Send to Funder" once a document is approved.
    WebUI.waitForElementNotPresent(back, 10, FailureHandling.OPTIONAL)
    WebUI.waitForElementVisible(readyToSend, 10, FailureHandling.OPTIONAL)
}

if (approved == 0) {
    KeywordUtil.markFailedAndStop('No "In Review" documents to approve — has the supplier finished step 8, or was step 9 already done?')
}
KeywordUtil.logInfo("Approved ${approved} required document(s)")

// Post-condition per SE-6150: every document is now "Ready to Send to Funder".
WebUI.verifyElementPresent(readyToSend, 20)
WebUI.verifyElementNotPresent(inReview, 5)

WebUI.switchToDefaultContent()

WebUI.closeBrowser()
