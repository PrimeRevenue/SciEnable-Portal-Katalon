import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import smoke.Onboarding

// SE-6154: GPM sends the approved required documents to the funder.
// The ticket (2015) describes ticking each document's checkbox; the page no longer has checkboxes —
// one "Send Documents" button above the table sends everything that is "Ready to Send to Funder"
// (found while recording 10/14).
String company = Onboarding.companyName(companyName)

TestObject itemFrame = Onboarding.byXpath('dashboardFrame', "//iframe[@scrolling='no']")
TestObject sendDocuments = Onboarding.byXpath('sendDocuments', "//*[" + Onboarding.cls('primary') + " and normalize-space(.)='Send Documents']")
TestObject readyToSend = Onboarding.byXpath('readyToSend', "//*[" + Onboarding.cls('blue') + " and normalize-space(.)='Ready to Send to Funder']")
TestObject funderReview = Onboarding.byXpath('funderReview', "//*[" + Onboarding.cls('yellow') + " and normalize-space(.)='Funder Review']")

Onboarding.loginGpm()
Onboarding.openSupplierAsGpm(company)

// GPM menu links have ids page_link_<n> (the number changes; self-healing found #page_link_220 on 10/14).
WebUI.click(Onboarding.byXpath('requiredDocsMenu', "//*[starts-with(@id, 'page_link_') and normalize-space(.)='Required Documents']"), FailureHandling.OPTIONAL)
WebUI.switchToFrame(itemFrame, 20)

// Step 9 must have run: documents are "Ready to Send to Funder" before sending, none still "In Review".
WebUI.verifyElementPresent(readyToSend, 20)
WebUI.verifyElementNotPresent(Onboarding.byXpath('inReviewDoc', "//*[" + Onboarding.cls('yellow') + " and normalize-space(.)='In Review']"), 5)

WebUI.click(sendDocuments)

// Post-condition per SE-6154: status changes to "Funder Review" for every document.
WebUI.verifyElementPresent(funderReview, 20)
WebUI.verifyElementNotPresent(readyToSend, 5)

WebUI.switchToDefaultContent()

WebUI.closeBrowser()
