<?xml version="1.0" encoding="UTF-8"?>
<Workflow xmlns="http://soap.sforce.com/2006/04/metadata">
    <fieldUpdates>
        <fullName>EZTestFUSai</fullName>
        <description>Test</description>
        <field>EZTestField__c</field>
        <name>EZTestFUSai</name>
        <notifyAssignee>false</notifyAssignee>
        <operation>Literal</operation>
        <protected>false</protected>
    </fieldUpdates>
    <rules>
        <fullName>EZTestRuleSai</fullName>
        <actions>
            <name>EZTestFUSai</name>
            <type>FieldUpdate</type>
        </actions>
        <active>false</active>
        <criteriaItems>
            <field>EZTest__c.LastModifiedById</field>
            <operation>notEqual</operation>
            <value>sai</value>
        </criteriaItems>
        <triggerType>onCreateOrTriggeringUpdate</triggerType>
    </rules>
</Workflow>
