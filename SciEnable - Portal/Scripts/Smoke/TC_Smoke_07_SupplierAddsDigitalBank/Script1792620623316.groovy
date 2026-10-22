import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

import internal.GlobalVariable
import smoke.Onboarding

// SE-17827: supplier adds a digital bank account through Plaid, authorizes it and attaches a
// supporting document. Only the outlookauthorizeflow program has the digital (Plaid) bank flow;
// OutlookBuyer has the traditional one (step 6), so skip there.
if ((GlobalVariable.smokeDesiredProgram ?: 'OutlookBuyer') != 'outlookauthorizeflow') {
    KeywordUtil.markWarning('Skipped: the digital bank flow needs smokeDesiredProgram = outlookauthorizeflow')
    return
}
String email = Onboarding.supplierEmail(supplierEmail)

String document = new File(RunConfiguration.getProjectDir(), 'Data Files/smoke/smoke-test-document.pdf').absolutePath

// Bank Account Information loads in bank-acct-frame; Plaid Link is an iframe inside that frame, and
// Plaid's sandbox bank (First Platypus) logs in through its own popup window (recorded 10/21).
TestObject bankFrame = Onboarding.byXpath('bankFrame', "//iframe[@id='bank-acct-frame']")
TestObject plaidFrame = Onboarding.byXpath('plaidFrame', "//iframe[starts-with(@id, 'plaid-link-iframe')]")

TestObject byTestId(String id) {
    return Onboarding.byXpath(id, "//*[@data-testid='" + id + "']")
}

TestObject button(String label) {
    return Onboarding.byXpath('button_' + label, "//button[normalize-space(.)='" + label + "']")
}

void scriptClick(TestObject to) {
    WebUI.executeJavaScript('arguments[0].click()', Arrays.asList(WebUI.findWebElement(to, 10)))
}

// The test bank's checkboxes are hidden; you tick one by clicking its row (a label), and the page
// records it as a "checked" attribute on the hidden input. Its Continue / Connect buttons stay
// disabled until the right rows are ticked. Skip rows already ticked (a click would untick them).
void selectRow(TestObject row) {
    WebUI.waitForElementVisible(row, 20)
    WebElement box = WebUI.findWebElement(row, 10).findElement(By.cssSelector('input.checkbox'))
    if (!box.getAttribute('checked')) {
        scriptClick(row)
    }
}

Onboarding.loginSupplier(email)
Onboarding.openChecklistItem('BankAccountInformation')
WebUI.switchToFrame(bankFrame, 20)

// 1. New account, country, then Plaid ("Connect Instantly")
WebUI.click(button('Add New Account'))
WebUI.click(Onboarding.byXpath('countrySelect', "(//*[" + Onboarding.cls('ant-select-selector') + "])[1]"))
// The country list only draws the options in view (Austria, Belgium, Canada... — 10/22 run), so
// type into its search box to bring United States into the list first.
WebUI.setText(Onboarding.byXpath('countrySearch', "(//*[" + Onboarding.cls('ant-select-selector') + "])[1]//input"), 'United States')
WebUI.click(Onboarding.byXpath('countryUsa', "//*[@label='United States Of America']//*[" + Onboarding.cls('ant-select-item-option-content') + "]"))
WebUI.click(Onboarding.byXpath('connectInstantly', "//*[@id='plaidConnectInstantly']"))

// 2. Plaid Link: no phone number, pick the bank, continue to its login
WebUI.switchToFrame(plaidFrame, 30)
WebUI.click(byTestId('button-combined-consent-continue-without-phone'))
WebUI.click(Onboarding.byXpath('selectBank', "//*[@aria-label='Select Bank of America']"))
WebUI.click(byTestId('button-oauth-continue'))

// 3. Plaid's sandbox bank login, in a popup. Made-up sandbox values (10/21 recording), not real
// credentials.
WebUI.switchToWindowTitle('First Platypus Bank - OAuth Login Page')
TestObject username = Onboarding.byXpath('plaidUsername', "//input[@id='username']")
WebUI.waitForElementVisible(username, 20)
WebUI.setText(username, 'user_good')
WebUI.setText(Onboarding.byXpath('plaidPassword', "//input[@id='password']"), 'pass_good')
// How this page works (read from Plaid's sandbox-oauth.js, 10/22): every step is already in the
// HTML and shown in turn. "Sign in" loads the accounts in the background and moves on to "Get code"
// when that finishes - and in the 10/22 runs the page sometimes jumped back a step after we had
// moved on. So act on whichever step is showing until the account list appears. Any code is accepted.
WebUI.click(Onboarding.byXpath('plaidSignIn', "//*[@id='submit-credentials']"))
TestObject getCode = Onboarding.byXpath('plaidGetCode', "//*[@id='submit-device']")
TestObject code = Onboarding.byXpath('plaidCode', "//input[@id='code']")
TestObject submitCode = Onboarding.byXpath('plaidSubmitCode', "//*[@id='submit-code']")
TestObject checkingRow = Onboarding.byXpath('plaidCheckingRow', "//label[" + Onboarding.cls('selection') + " and contains(normalize-space(.), 'Plaid Checking')]")
// A normal click on "Get code" silently did nothing in the small popup (10/22: clicked 10 times, page
// never moved on), so press these buttons with a script click, and log which step is showing.
String stepShowing = "return ['credentials','verify-device','verify-code','account-select','confirm']" +
    ".filter(function (id) { var e = document.getElementById(id); return e && e.offsetParent !== null }).join(',')"
boolean atAccounts = false
for (int i = 0; i < 10 && !atAccounts; i++) {
    KeywordUtil.logInfo('Plaid test bank step showing: ' + WebUI.executeJavaScript(stepShowing, null))
    if (WebUI.verifyElementVisible(getCode, FailureHandling.OPTIONAL)) {
        scriptClick(getCode)
    } else if (WebUI.verifyElementVisible(code, FailureHandling.OPTIONAL)) {
        WebUI.setText(code, '1234')
        scriptClick(submitCode)
    }
    atAccounts = WebUI.waitForElementVisible(checkingRow, 3, FailureHandling.OPTIONAL)
}
if (!atAccounts) {
    KeywordUtil.markFailedAndStop('Plaid test bank never reached the account list (Sign in > Get code > code)')
}
selectRow(Onboarding.byXpath('plaidChecking', "//label[" + Onboarding.cls('selection') + " and contains(normalize-space(.), 'Plaid Checking')]"))
selectRow(Onboarding.byXpath('shareNames', "//label[@for='name']"))
selectRow(Onboarding.byXpath('shareAccountNumber', "//label[@for='account-number']"))
scriptClick(Onboarding.byXpath('plaidAccountsContinue', "//*[@id='submit-accounts']"))
selectRow(Onboarding.byXpath('plaidTerms', "//*[@id='terms-label']"))
scriptClick(Onboarding.byXpath('plaidConfirm', "//*[@id='submit-confirmation']"))

// 4. The popup closes; back in Plaid Link (main window > bank frame > Plaid frame).
WebUI.switchToWindowIndex(0)
WebUI.switchToFrame(bankFrame, 20)
WebUI.switchToFrame(plaidFrame, 30)
WebUI.click(byTestId('button-account-selection-continue'))
// Plaid asks to save a phone number, then for a code sent to it; sandbox accepts made-up values.
WebUI.setText(byTestId('input-phone'), '2345678999')
WebUI.click(byTestId('button-end-of-flow-phone-input-submit'))
WebUI.setText(byTestId('input-otp'), '123456')

// 5. Plaid closes and the bank form shows "Bank added"; fill in the bank's address.
WebUI.switchToDefaultContent()
WebUI.switchToFrame(bankFrame, 20)
WebUI.verifyElementPresent(Onboarding.byXpath('bankAdded', "//*[" + Onboarding.cls('ant-notification-notice-description') + " and normalize-space(.)='Bank added']"), 30)
WebUI.setText(Onboarding.byXpath('street1', "//input[@id='street1' or @name='street1']"), '1 Citizens Drive')
// State: a searchable dropdown; type to bring Rhode Island into its (scrolling) option list.
WebUI.click(Onboarding.byXpath('stateSelect', "//*[" + Onboarding.cls('ant-form-item') + "][.//label[contains(normalize-space(.), 'State')]]//*[" + Onboarding.cls('ant-select-selector') + "]"))
WebUI.setText(Onboarding.byXpath('stateSearch', "//*[" + Onboarding.cls('ant-form-item') + "][.//label[contains(normalize-space(.), 'State')]]//input"), 'Rhode Island', FailureHandling.OPTIONAL)
WebUI.click(Onboarding.byXpath('stateRhodeIsland', "//*[@label='Rhode Island']//*[" + Onboarding.cls('ant-select-item-option-content') + "]"))
WebUI.setText(Onboarding.byXpath('city', "//input[@id='city']"), 'Riverside')
WebUI.setText(Onboarding.byXpath('zipCode', "//input[@id='zipCode']"), '02915')
// 6. After the address come two more screens: one with its own "Next >" (the ">" clicked in the
// recording is that button's arrow), then "Authorize & Next". Clicking the arrow straight after the
// address Next didn't advance (10/22) - and self-healing then clicked "Next >" for the missing
// Authorize button. So click Next until Authorize shows.
TestObject bankNext = Onboarding.byXpath('bankNext', "//button[" + Onboarding.cls('ant-btn-primary') + " and normalize-space(.)='Next']")
TestObject authorize = Onboarding.byXpath('authorize', "//button[contains(normalize-space(.), 'Authorize')]")
for (int i = 0; i < 6 && !WebUI.waitForElementVisible(authorize, 3, FailureHandling.OPTIONAL); i++) {
    if (WebUI.verifyElementVisible(bankNext, FailureHandling.OPTIONAL)) {
        WebUI.click(bankNext)
    }
}
WebUI.click(authorize)
WebUI.click(Onboarding.byXpath('attachSupportingDoc', "//button[contains(normalize-space(.), 'Attach Supporting Document')]"))
WebUI.uploadFile(Onboarding.byXpath('supportingDoc', "//input[@type='file']"), document)
WebUI.click(Onboarding.byXpath('attach', "//*[@role='dialog']//button[" + Onboarding.cls('ant-btn-primary') + " and normalize-space(.)='Attach']"))
WebUI.click(Onboarding.byXpath('complete', "//button[" + Onboarding.cls('btn-submit') + " and normalize-space(.)='Complete']"))

// Post-condition: the new bank account is listed "In Review" (waiting for the GPM, step 10).
WebUI.verifyElementPresent(Onboarding.byXpath('bankInReview', "//*[" + Onboarding.cls('ant-table-row') + "]//*[" + Onboarding.cls('ant-tag') + " and normalize-space(.)='In Review']"), 30)

WebUI.switchToDefaultContent()

WebUI.closeBrowser()
