import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebElement

import smoke.Onboarding

// SE-15104: supplier accepts the Online Supplier Agreement.
String email = Onboarding.supplierEmail(supplierEmail)

Onboarding.loginSupplier(email)

Onboarding.openChecklistItem('supplier_user_legal_agreement_view')

// Tick every agreement checkbox. Their names carry the agreement's ID (accept_31790 when recorded
// 10/13), which can differ by program or agreement version, so match the prefix instead.
List<WebElement> boxes = WebUI.findWebElements(Onboarding.byXpath('agreementBoxes', "//input[@type='checkbox' and starts-with(@name, 'accept_')]"), 20)
if (boxes.isEmpty()) {
    com.kms.katalon.core.util.KeywordUtil.markFailedAndStop('No agreement checkbox found on the Online Supplier Agreement page')
}
for (WebElement box : boxes) {
    if (!box.isSelected()) {
        box.click()
    }
}

// "Do you have legal authority…?" -> Yes
WebUI.click(Onboarding.byXpath('legalAuthorityYes', "//a[contains(@href, '/confirm_legal_authority') and contains(@href, 'id=yes')]"))

WebUI.click(Onboarding.byXpath('legalSubmit', "//*[@id='legal_submit']"))

Onboarding.verifyCompleted(['Online Supplier Agreement'])

WebUI.closeBrowser()
