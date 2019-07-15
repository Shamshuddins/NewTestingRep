package main.java.module;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.openqa.selenium.WebElement;

import main.java.framework.UIDriver;
import main.java.framework.Utility;

public class PSToolUtility {
    static Map<String, String> projectStatementDataMap = null;
    static Map<String, String> projectStatementValidateDataMap = null;
    static List<String> selectedCapabilityList = null;
    static List<String> selectedPhasesList = null;
    static Map<String, String> positionRateDataMap = null;
    static Map<String, String> positionRateValidateDataMap = null;
    /**
        * @description Store the project data into a data map from the CreatePS page.
    */
    public static boolean validateProjectStmtData(String source) throws InterruptedException {
        projectStatementValidateDataMap = projectStatementDataMap;
        //projectStatementValidateDataMap = new LinkedHashMap<String, String>();
        //projectStatementValidateDataMap.putAll(projectStatementDataMap);
        boolean bPool = true;
        String projectStmtDataElementInit;
        UIDriver.wait(2);
        if(source.equalsIgnoreCase("Agency") || source.equalsIgnoreCase("MarketerReview")) {
            projectStatementValidateDataMap.put("ProjectName", UIDriver.getElement("projectNameDiv").getText());
            projectStmtDataElementInit = UIDriver.getElementProperty("projectStatementDataElement");

            List<String> attributeList = new ArrayList<String>() {{
                add("Agency");
                add("TCCC Project Lead");
                add("Agency Project Lead");
                add("Project Start Date");
                add("Project End Date");
                add("Brand");
                add("Compensation Structure");
            }};

            for (String attribute : attributeList) {
                String projectStmtDataElement = projectStmtDataElementInit.replaceAll("DataAttributePlaceHolder", attribute);
                projectStatementValidateDataMap.put(attribute, UIDriver.getElementByProperty(projectStmtDataElement).getText().split("\\r?\\n")[0]);
            }
        }
        else if(source.equalsIgnoreCase("Marketer")){
            projectStmtDataElementInit = UIDriver.getElementProperty("overviewDataLabel");
            projectStatementValidateDataMap.put("ProjectName", UIDriver.getElement("overviewProjectName").getText());
            List<String> attributeList = new ArrayList<String>() {{
                add("Agency");
                add("TCCC Project Lead");
                add("Cluster Charter");
                add("Compensation Structure");
            }};

            for (String attribute : attributeList) {
                String projectStmtDataElement = projectStmtDataElementInit.replaceAll("LabelPlaceHolder", attribute);
                projectStatementValidateDataMap.put(attribute, UIDriver.getElementByProperty(projectStmtDataElement).getText().split("\\r?\\n")[0]);
            }

            projectStatementValidateDataMap.put("Agency Project Lead", UIDriver.getElementText("overviewAgencyProjectLead"));
            projectStatementValidateDataMap.put("Project Summary", UIDriver.getElementText("overviewProjectSummary"));
            projectStatementValidateDataMap.put("Brand", UIDriver.getElementText("overviewBrand"));

            try {
                SimpleDateFormat src = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat target = new SimpleDateFormat("dd MMM. yyyy");

                UIDriver.clickElement("overviewStartDatePicker");
                UIDriver.wait(2);
                String selectedDate = UIDriver.getElementAttribute("overviewSelectedDate", "data-datevalue");
                System.out.println(selectedDate);
                projectStatementValidateDataMap.put("Project Start Date", target.format(src.parse(selectedDate)));

                UIDriver.clickElement("overviewEndDatePicker");
                UIDriver.wait(2);
                selectedDate = UIDriver.getElementAttribute("overviewSelectedDate", "data-datevalue");
                System.out.println(selectedDate);
                projectStatementValidateDataMap.put("Project End Date", target.format(src.parse(selectedDate)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            String projectStmtDataLabelInit = UIDriver.getElementProperty("deliverableProjectStatementHeader");

            String attribute = "Project Name";
            String projectStmtDataLabel = projectStmtDataLabelInit.replaceAll("LabelPlaceHolder", attribute);
            projectStatementValidateDataMap.put("ProjectName", UIDriver.getElementByProperty(projectStmtDataLabel).getText().split(":")[1].trim());

            attribute = "Agency";
            projectStmtDataLabel = projectStmtDataLabelInit.replaceAll("LabelPlaceHolder", attribute);
            projectStatementValidateDataMap.put(attribute, UIDriver.getElementByProperty(projectStmtDataLabel).getText().split(":")[1].trim());

            attribute = "Agency Lead";
            projectStmtDataLabel = projectStmtDataLabelInit.replaceAll("LabelPlaceHolder", attribute);
            projectStatementValidateDataMap.put("Agency Project Lead", UIDriver.getElementByProperty(projectStmtDataLabel).getText().split(":")[1].trim());

            attribute = "Status";
            projectStmtDataLabel = projectStmtDataLabelInit.replaceAll("LabelPlaceHolder", attribute);
            bPool = UIDriver.getElementByProperty(projectStmtDataLabel).getText().split(":")[1].trim().equalsIgnoreCase("Completion by Marketer");
            try {
                SimpleDateFormat src = new SimpleDateFormat("MMMM dd, yyyy");
                SimpleDateFormat target = new SimpleDateFormat("dd MMM. yyyy");
                projectStmtDataLabelInit = UIDriver.getElementProperty("deliverableProjectStatementHeaderDate");
                attribute = "Start Date";
                projectStmtDataLabel = projectStmtDataLabelInit.replaceAll("LabelPlaceHolder", attribute);
                String selectedDate = UIDriver.getElementByProperty(projectStmtDataLabel).getText();
                projectStatementValidateDataMap.put("Project Start Date", target.format(src.parse(selectedDate)));

                attribute = "End Date";
                projectStmtDataLabel = projectStmtDataLabelInit.replaceAll("LabelPlaceHolder", attribute);
                selectedDate = UIDriver.getElementByProperty(projectStmtDataLabel).getText();
                projectStatementValidateDataMap.put("Project End Date", target.format(src.parse(selectedDate)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(projectStatementDataMap);
        //System.out.println(projectStatementValidateDataMap);
        return projectStatementDataMap.equals(projectStatementValidateDataMap) && bPool;
    }
    /**
     * @description Store the hourly rate into a data map from the rates page.
     */
    public static void storePositionHourlyRate() {
        positionRateDataMap = new LinkedHashMap<String, String>();
        List<String> positionList = Utility.getElementText("positionList");
        List<String> rateList = Utility.getElementText("rateList");
        for(String position : positionList){
            positionRateDataMap.put(position, rateList.get(positionList.indexOf(position)));
        }

        System.out.println(positionRateDataMap);
    }
    /**
     * @description validate selected rate card against the rate list displayed on Staffing page.
     */
    public static boolean validatePositionHourlyRate() {
        positionRateValidateDataMap = new LinkedHashMap<String, String>();
        List<String> positionList = Utility.getElementText("staffingPositionList");
        List<String> rateList = Utility.getElementText("staffingRateList");
        for(String position : positionList){
            positionRateValidateDataMap.put(position, rateList.get(positionList.indexOf(position)));
        }
        return positionRateValidateDataMap.equals(positionRateDataMap);
    }
}

