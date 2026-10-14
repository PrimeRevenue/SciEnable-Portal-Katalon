import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling

import smoke.Onboarding

// SE-15119: supplier uploads every required (KYC) document. The outlook program lists two
// ("outlook kyc", "Roger kyc" when recorded 10/13); the loop handles however many there are.
String email = Onboarding.supplierEmail(supplierEmail)

// Upload straight into the file input — the recorder only saw the browser's masked "C:\fakepath\..." path.
String document = new File(RunConfiguration.getProjectDir(), 'Data Files/smoke/smoke-test-document.pdf').absolutePath

// The required-documents list and each document's page load inside the checklist's inner frame.
TestObject itemFrame = Onboarding.byXpath('requiredDocsFrame', "//iframe[@scrolling='no']")
TestObject getStarted = Onboarding.byXpath('getStarted', "//*[" + Onboarding.cls('green') + " and normalize-space(.)='Get Started']")
TestObject uploadInput = Onboarding.byXpath('uploadInput', "//input[@id='required-document-upload']")
TestObject uploaded = Onboarding.byXpath('uploadedMessage', "//*[" + Onboarding.cls('content') + " and contains(normalize-space(.), 'File(s) successfully uploaded')]")
TestObject next = Onboarding.byXpath('docNext', "//a[" + Onboarding.cls('primary') + " and normalize-space(.)='Next']")

Onboarding.loginSupplier(email)

int uploadedCount = 0
for (int i = 0; i < 10; i++) {
    Onboarding.openChecklistItem('supplier_user_required_documents')
    WebUI.switchToFrame(itemFrame, 20)
    // Documents still to do show a green "Get Started"; stop once none are left.
    if (!WebUI.waitForElementVisible(getStarted, 10, FailureHandling.OPTIONAL)) {
        break
    }
    WebUI.click(getStarted)
    WebUI.uploadFile(uploadInput, document)
    WebUI.verifyElementPresent(uploaded, 20)
    WebUI.click(next)
    uploadedCount++
}

if (uploadedCount == 0) {
    KeywordUtil.markFailedAndStop('No required documents to upload (no "Get Started" button) — was this supplier already used for step 8?')
}
KeywordUtil.logInfo("Uploaded ${uploadedCount} required document(s)")

WebUI.switchToDefaultContent()

WebUI.closeBrowser()
