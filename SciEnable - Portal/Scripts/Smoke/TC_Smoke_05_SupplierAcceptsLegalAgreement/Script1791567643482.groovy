import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import smoke.Onboarding

// SE-15104: supplier accepts the Online Supplier Agreement.
String email = Onboarding.supplierEmail(supplierEmail)

Onboarding.loginSupplier(email)

Onboarding.openChecklistItem('supplier_user_legal_agreement_view')

// Tick every agreement checkbox. Their names carry the agreement's ID (accept_31790 when recorded
// 10/13), which can differ by program or agreement version, so match the prefix instead.
String boxesXpath = "//input[@type='checkbox' and starts-with(@name, 'accept_')]"
int count = WebUI.findWebElements(Onboarding.byXpath('agreementBoxes', boxesXpath), 20).size()
if (count == 0) {
    KeywordUtil.markFailedAndStop('No agreement checkbox found on the Online Supplier Agreement page')
}

// Ticking a box opens "Do you have legal authority…?" over the page (a blackBoxOverlay). With one
// agreement (OutlookBuyer) that was fine; outlookauthorizeflow has several, and the open popup
// blocked the next box (10/20). So answer Yes after each tick, before the next one.
TestObject legalAuthorityYes = Onboarding.byXpath('legalAuthorityYes', "//a[contains(@href, '/confirm_legal_authority') and contains(@href, 'id=yes')]")
TestObject overlay = Onboarding.byXpath('blackBoxOverlay', "//*[" + Onboarding.cls('blackBoxOverlay') + "]")
for (int i = 1; i <= count; i++) {
    TestObject box = Onboarding.byXpath('agreementBox' + i, '(' + boxesXpath + ')[' + i + ']')
    if (!WebUI.verifyElementChecked(box, 5, FailureHandling.OPTIONAL)) {
        WebUI.click(box)
    }
    if (WebUI.waitForElementVisible(legalAuthorityYes, 5, FailureHandling.OPTIONAL)) {
        WebUI.click(legalAuthorityYes)
        WebUI.waitForElementNotVisible(overlay, 10, FailureHandling.OPTIONAL)
    }
}
KeywordUtil.logInfo("Accepted ${count} agreement(s)")

// All boxes must still be ticked before submitting.
for (int i = 1; i <= count; i++) {
    WebUI.verifyElementChecked(Onboarding.byXpath('agreementBox' + i, '(' + boxesXpath + ')[' + i + ']'), 5)
}

WebUI.click(Onboarding.byXpath('legalSubmit', "//*[@id='legal_submit']"))

Onboarding.verifyCompleted(['Online Supplier Agreement'])

WebUI.closeBrowser()
