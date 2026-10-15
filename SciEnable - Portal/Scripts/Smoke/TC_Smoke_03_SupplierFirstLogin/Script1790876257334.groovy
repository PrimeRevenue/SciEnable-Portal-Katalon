import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import smoke.GmailReader

// SE-15102: supplier sets a password from the email link, accepts the privacy policy, lands on home.
// The link comes from the welcome email in the GMAILUSERNAME inbox (read over IMAP, inbox + Spam).
// A link pasted into the setPasswordLink variable is used instead, for running this step on its own.
// trim(): values pasted from Gmail can carry a hidden tab/line break.
setPasswordLink = setPasswordLink?.trim()
if (!setPasswordLink) {
    String supplierEmail = GlobalVariable.smokeSupplierEmail
    if (!supplierEmail) {
        KeywordUtil.markFailedAndStop('Run via the smoke suite (to read the welcome email), or paste a Password Set Up link into the setPasswordLink variable')
    }
    // Each run's supplier has its own +alias address, so the recipient alone identifies the right
    // email; the 6-hour window just keeps the search small.
    setPasswordLink = GmailReader.waitForValue(GlobalVariable.GMAILUSERNAME, GlobalVariable.GMAILAPPPASSWORD,
        supplierEmail, '', 'Password Set Up:\\s*(https://auth-reg\\.primerevenue\\.com/[^\\s"<>]+)',
        new Date(System.currentTimeMillis() - 6L * 60 * 60 * 1000), 600)
}
if (!(setPasswordLink?.startsWith('https://auth-reg.primerevenue.com/'))) {
    KeywordUtil.markFailedAndStop('Paste a fresh Password Set Up / Reset Password link from the supplier email into the setPasswordLink variable first')
}

WebUI.openBrowser(null)

WebUI.navigateToUrl(setPasswordLink)

// An expired/used link lands on "Forgot password" instead of the change-password form.
if (WebUI.verifyTextPresent('Your password reset code has expired or is invalid', false, FailureHandling.OPTIONAL)) {
    KeywordUtil.markFailedAndStop('The set-password link has expired or was already used — request a fresh one')
}

// setText, not setEncryptedText: protected variables arrive as plain text (see TC_Smoke_02).
WebUI.setText(findTestObject('Page_Regression - SCiEnable/input_Password'), GlobalVariable.smokeSupplierPassword)

WebUI.setText(findTestObject('Page_Regression - SCiEnable/input_Confirm password'), GlobalVariable.smokeSupplierPassword)

// The login page's button_Submit object (class "submit") doesn't match this page's button — self-healing
// had to rescue it on 10/14 — so locate it by its label.
WebUI.click(byXpath('setPasswordSubmit', "//button[normalize-space(.)='Submit']"))

// Privacy acknowledgement
WebUI.click(findTestObject('Page_SE/label_I agree to the processing of my personal d'))

WebUI.click(findTestObject('Page_SE/div_continue'))

// Post-condition per SE-15102: supplier home page with the onboarding steps.
WebUI.verifyElementPresent(byXpath('thankYouRegistering', '//span[starts-with(normalize-space(.), \'Thank you for registering for the outlook buyer\')]'), 
    20)

WebUI.verifyElementPresent(byXpath('onboardingChecklist', '//*[contains(concat(\' \', normalize-space(@class), \' \'), \' boarding \') and normalize-space(.)=\'Onboarding Checklist\']'), 
    10)

WebUI.closeBrowser()

TestObject byXpath(String name, String xpath) {
    TestObject to = new TestObject(name)

    to.addProperty('xpath', ConditionType.EQUALS, xpath)

    return to
}

