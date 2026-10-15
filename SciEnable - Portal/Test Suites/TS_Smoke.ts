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
   <testCaseLink>
      <guid>b3a061bd-4d70-4de4-8e48-e196af772a7a</guid>
      <isReuseDriver>false</isReuseDriver>
      <isRun>true</isRun>
      <testCaseId>Test Cases/Smoke/TC_Smoke_03_SupplierFirstLogin</testCaseId>
   </testCaseLink>
   <testCaseLink>
      <guid>44ed03be-7236-4a88-8076-adfe813cc575</guid>
      <isReuseDriver>false</isReuseDriver>
      <isRun>true</isRun>
      <testCaseId>Test Cases/Smoke/TC_Smoke_04_SupplierCompletesChecklist</testCaseId>
   </testCaseLink>
   <testCaseLink>
      <guid>7fbf3b67-06e7-4aad-b6f1-1ed26561a430</guid>
      <isReuseDriver>false</isReuseDriver>
      <isRun>true</isRun>
      <testCaseId>Test Cases/Smoke/TC_Smoke_05_SupplierAcceptsLegalAgreement</testCaseId>
   </testCaseLink>
   <testCaseLink>
      <guid>7f0824ee-5916-4db5-b7c7-7b47db36aa38</guid>
      <isReuseDriver>false</isReuseDriver>
      <isRun>true</isRun>
      <testCaseId>Test Cases/Smoke/TC_Smoke_08_SupplierUploadsRequiredDocs</testCaseId>
   </testCaseLink>
   <testCaseLink>
      <guid>8eae7497-bead-4c85-a87f-4c89f9bbfd7c</guid>
      <isReuseDriver>false</isReuseDriver>
      <isRun>true</isRun>
      <testCaseId>Test Cases/Smoke/TC_Smoke_09_GPMApprovesRequiredDocs</testCaseId>
   </testCaseLink>
   <testCaseLink>
      <guid>c43e87cd-9319-4b54-9761-0c2589a4cdb9</guid>
      <isReuseDriver>false</isReuseDriver>
      <isRun>true</isRun>
      <testCaseId>Test Cases/Smoke/TC_Smoke_11_GPMSendsDocsToFunder</testCaseId>
   </testCaseLink>
</TestSuiteEntity>
