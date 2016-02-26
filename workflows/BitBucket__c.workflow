<?xml version="1.0" encoding="UTF-8"?>
<Workflow xmlns="http://soap.sforce.com/2006/04/metadata">
    <alerts>
        <fullName>EmailAlert</fullName>
        <description>EmailAlert</description>
        <protected>false</protected>
        <recipients>
            <recipient>pavan2mandadi@gmail.com</recipient>
            <type>user</type>
        </recipients>
        <senderType>CurrentUser</senderType>
        <template>unfiled$public/MarketingProductInquiryResponse</template>
    </alerts>
    <fieldUpdates>
        <fullName>Field_Update</fullName>
        <description>Client issue Test</description>
        <field>Updated_by__c</field>
        <formula>UPPER(&quot;test&quot;)</formula>
        <name>schneider Field Update</name>
        <notifyAssignee>false</notifyAssignee>
        <operation>Formula</operation>
        <protected>false</protected>
    </fieldUpdates>
    <outboundMessages>
        <fullName>OutBound_Message</fullName>
        <apiVersion>35.0</apiVersion>
        <description>Client Issue</description>
        <endpointUrl>https://www.google.co.in/</endpointUrl>
        <fields>Id</fields>
        <fields>IsDeleted</fields>
        <fields>LastModifiedById</fields>
        <includeSessionId>false</includeSessionId>
        <integrationUser>pavan2mandadi@gmail.com</integrationUser>
        <name>OutBound Message</name>
        <protected>false</protected>
        <useDeadLetterQueue>false</useDeadLetterQueue>
    </outboundMessages>
    <rules>
        <fullName>Update ph no</fullName>
        <active>false</active>
        <criteriaItems>
            <field>BitBucket__c.Phone_no__c</field>
            <operation>equals</operation>
            <value>1234567890</value>
        </criteriaItems>
        <criteriaItems>
            <field>BitBucket__c.Updated_by__c</field>
            <operation>startsWith</operation>
            <value>Pavan</value>
        </criteriaItems>
        <criteriaItems>
            <field>User.AboutMe</field>
            <operation>equals</operation>
            <value>test</value>
        </criteriaItems>
        <description>This is to update ph no</description>
        <triggerType>onCreateOrTriggeringUpdate</triggerType>
    </rules>
</Workflow>
