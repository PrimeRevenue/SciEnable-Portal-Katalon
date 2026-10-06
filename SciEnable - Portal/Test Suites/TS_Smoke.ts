<?xml version="1.0" encoding="UTF-8"?>
<TestSuiteEntity>
   <description>SE supplier-onboarding smoke flow (Sapna's smoke suite, SE-15096 onward). Steps run in order and share one new supplier per run via the smokeCompanyName / smokeSupplierEmail profile variables set by TC_Smoke_01. Run with the Regression profile, on VPN. Each run creates a real supplier on Regression — don't schedule without a cleanup plan.</description>
   <name>TS_Smoke</name>
   <tag></tag>
   <isRerun>false</isRerun>
   <mailRecipient></mailRecipient>
   <maxConcurrentInstances>1</maxConcurrentInstances>
   <numberOfRerun>0</numberOfRerun>
   <orchestration>CLASSIC</orchestration>
   <pageLoadTimeout>10</pageLoadTimeout>
   <pageLoadTimeoutDefault>true</pageLoadTimeoutDefault>
   <rerunFailedTestCasesOnly>false</rerunFailedTestCasesOnly>
   <rerunImmediately>false</rerunImmediately>
   <testSuiteGuid>18cf4e67-092e-4ed3-bf61-723580a77c41</testSuiteGuid>
   <testCaseLink>
      <guid>4df31a29-fb41-4b05-ab83-222e6e29e67f</guid>
      <isReuseDriver>false</isReuseDriver>
      <isRun>true</isRun>
      <testCaseId>Test Cases/Smoke/TC_Smoke_01_SupplierRegistration</testCaseId>
   </testCaseLink>
   <testCaseLink>
      <guid>20e9c771-1220-4c20-85c6-25f90a206516</guid>
      <isReuseDriver>false</isReuseDriver>
      <isRun>true</isRun>
      <testCaseId>Test Cases/Smoke/TC_Smoke_02_GPMValidatesSupplier</testCaseId>
   </testCaseLink>
</TestSuiteEntity>
