<?xml version="1.0" encoding="UTF-8"?>
<Workflow xmlns="http://soap.sforce.com/2006/04/metadata">
    <fieldUpdates>
        <fullName>EZTest_demo_FU</fullName>
        <field>EZ2__c</field>
        <name>EZTest demo FU</name>
        <notifyAssignee>false</notifyAssignee>
        <operation>Literal</operation>
        <protected>false</protected>
    </fieldUpdates>
    <fieldUpdates>
        <fullName>EZ_Test_FU_Sai</fullName>
        <description>Test</description>
        <field>EZ__c</field>
        <name>EZ Test FU Sai</name>
        <notifyAssignee>false</notifyAssignee>
        <operation>Literal</operation>
        <protected>false</protected>
    </fieldUpdates>
    <fieldUpdates>
        <fullName>Pavan_Rule</fullName>
        <field>EZ__c</field>
        <name>Pavan Rule</name>
        <notifyAssignee>false</notifyAssignee>
        <operation>Literal</operation>
        <protected>false</protected>
    </fieldUpdates>
    <fieldUpdates>
        <fullName>SaiTest_new</fullName>
        <field>EZ2__c</field>
        <name>SaiTest new</name>
        <notifyAssignee>false</notifyAssignee>
        <operation>Literal</operation>
        <protected>false</protected>
    </fieldUpdates>
    <rules>
        <fullName>EZ Test WR</fullName>
        <actions>
            <name>EZ_Test_FU_Sai</name>
            <type>FieldUpdate</type>
        </actions>
        <active>false</active>
        <criteriaItems>
            <field>EZ2__c.EZ3__c</field>
            <operation>equals</operation>
            <value>test</value>
        </criteriaItems>
        <triggerType>onCreateOrTriggeringUpdate</triggerType>
    </rules>
    <rules>
        <fullName>EZTest demo</fullName>
        <actions>
            <name>EZTest_demo_FU</name>
            <type>FieldUpdate</type>
        </actions>
        <actions>
            <name>Pavan_Rule</name>
            <type>FieldUpdate</type>
        </actions>
        <active>false</active>
        <criteriaItems>
            <field>EZ2__c.EZ__c</field>
            <operation>equals</operation>
            <value>test</value>
        </criteriaItems>
        <description>Test</description>
        <triggerType>onCreateOrTriggeringUpdate</triggerType>
    </rules>
</Workflow>
