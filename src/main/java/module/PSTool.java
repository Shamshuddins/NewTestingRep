package main.java.module;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import freemarker.template.utility.StringUtil;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import main.java.framework.CSVReporter;
import main.java.framework.Framework;
import main.java.framework.UIDriver;
import main.java.framework.Utility;
import main.java.framework.BrowserDriver;
import java.util.Random;


@Listeners(main.java.listener.Listener.class)
public class PSTool extends BaseTest {
    public static String userRole;
    public static String capabilityName;
    public static String compensationFee;
    public static int deliverableCount = 7;

    public PSTool() {

        Method[] arrMethods = this.getClass().getDeclaredMethods();
        Framework.allMethods.add(Framework.iMethod, arrMethods);
        Framework.allDrivers.add(Framework.iDriver, this);
        Framework.iMethod++;
        Framework.iDriver++;
        PSToolUtility.projectStatementDataMap = new LinkedHashMap<String, String>();
    }

    // ==============Component starts from here===============


    /**
     * @name openURL
     * @author Sujata Padhi
     * @description Open the URL given in data file
     * @preCondition
     * @lastChanged
     * @TODO
     */
    @Parameters({"DataRowID"})
    @Test
    public static void openURL(@Optional String DataRowID) throws Exception {
        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        Map<String, String> input = Framework.getDataRecord(DataRowID, componentName);
        //String strURL = input.get("URL");
        //get data from environment.property file

        String strURL = prop.getProperty(strEnv.toUpperCase( ) + "_URL");
        UIDriver.launchURL(strURL);
        boolean bLoginPage = UIDriver.checkElementpresent("logoSalesForce", 15);
        CSVReporter.reportPassFail(bLoginPage, "openURL",
                "Screen should navigate to Login", "Navigation Successful", "Navigation Failed");
        Assert.assertEquals(bLoginPage, true, "Navigation Failed");
    }

    /**
     * @name
     * @author
     * @description
     * @preCondition
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})

    public static void psLogin(@Optional String DataRowID) {
        //Enter login credentials
        String strUserName = prop.getProperty(strEnv.toUpperCase( ) + "_" + DataRowID + "_username");
        String strPassword = prop.getProperty(strEnv.toUpperCase( ) + "_" + DataRowID + "_password");
        userRole = DataRowID;

        if (UIDriver.checkElementpresent("userName", 5) == true) {

            UIDriver.setValue("userName", strUserName);
            UIDriver.wait(1);
            UIDriver.setValue("passWord", strPassword);
            boolean bPass = UIDriver.checkElementpresent("Login", 1);
            CSVReporter.reportPassFail(bPass, "psLogin", "Login button should be present",
                    "Login button present", "Could not find Login button");
            Assert.assertEquals(bPass, true, "Navigation Failed");
            UIDriver.clickElement("Login");

            UIDriver.wait(2);
            boolean bError = !UIDriver.checkElementpresent("loginError", 1);

            if (DataRowID.equalsIgnoreCase("invalid")) {
                CSVReporter.reportPassFail(bError, "ccLogin", "Login Should Fail",
                        "Login Failed as Expected", "Login Passed unexpectedly");
            } else {
                CSVReporter.reportPassFail(bError, "ccLogin", "Login Should be Succesful",
                        "Login Successful", "Login Failed");
            }
           }
    }

    /**
     * @name: psHomePageValidation
     * @author :Sujata padhi
     * @description: Home page validation
     * @preCondition : User needs to login with a valid Marketer user
     * @lastChanged
     * @TODO
     */

    @Test
    @Parameters({"DataRowID"})

    public static void psHomePageValidation(@Optional String DataRowID) {

        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<String, String> input = Framework.getDataRecord(DataRowID, componentName);
        boolean bPass = false;


        bPass = UIDriver.checkElementpresent("homeLogo", 3);
        CSVReporter.reportPassFail(bPass, "Validation for homeLogo", "Home logo should be present in home page",
                "Logo is present", "Logo is not present");
        String userData = input.get("userData");
        String usernameOnHeader = UIDriver.getElementText("userNameonHeader");
        PSToolUtility.projectStatementDataMap.put("TCCC Project Lead", usernameOnHeader);

        Assert.assertEquals(userData, usernameOnHeader, "User Name not displayed on the header");
        CSVReporter.reportPassFail(usernameOnHeader.equals(userData),
                "psHomePageValidation",
                "The user's name should be displayed on the header", "User Name is displayed in the Header section",
                "User Name not displayed in the Header section");

        bPass = UIDriver.checkElementpresent("notificationBell", 1);
        CSVReporter.reportPassFail(bPass, "psHomePageValidation", "NotificationBell should be present in home page",
                "notificationBell is present", "NotificationBell is not present");
        //Create new button should not be visible for Agency user.
        bPass = UIDriver.checkElementpresent("createNewButton", 1);
        if (userRole.equalsIgnoreCase("Agency")) {
            CSVReporter.reportPassFail(!bPass, "psHomePageValidation",
                    "Create New Button should NOT be present in home page",
                    "Create New Button is NOT present",
                    "Create New Button is present");
        } else {
            CSVReporter.reportPassFail(bPass, "psHomePageValidation",
                    "Create New Button should be present in home page",
                    "Create New Button is present",
                    "Create New Button is not present");
        }
        bPass = UIDriver.checkElementpresent("selectLanguageBox", 1);
        CSVReporter.reportPassFail(bPass, "psHomePageValidation", "Select Language Box should be present in home page", "Select Language Box is present", "Select Language Box is not present");

        //open the user setting
        UIDriver.clickElement("userNameonHeader");
        bPass = UIDriver.checkElementpresent("logoutLink", 1);
        CSVReporter.reportPassFail(bPass, "psHomePageValidation", "Logout link should be present", "Logout link is present", "logout link is not present");
        bPass = UIDriver.checkElementpresent("userAccountSetting", 1);
        CSVReporter.reportPassFail(bPass, "psHomePageValidation", "Account Settings link should be present", "Account Settings link is present",
                "Account Settings link is not present");
        //close the popup
        UIDriver.clickElement("userNameonHeader");

        bPass = UIDriver.checkElementpresent("viewList", 1);
        CSVReporter.reportPassFail(bPass, "psHomePageValidation", "Item List view icon should be present", "Item List view icon is present",
                "Item List view icon is NOT present");

        bPass = UIDriver.checkElementpresent("viewGrid", 1);
        CSVReporter.reportPassFail(bPass, "psHomePageValidation", "Item Grid view icon should be present", "Item Grid view icon is present",
                "Item Grid view icon is NOT present");

        //Validation for Filters
        List<String> FilterType = Utility.webElementList(UIDriver.getElementProperty("filterBy"), "text", "0");
        String strFilterOptions = String.join("|", FilterType);
        CSVReporter.reportPassFail(strFilterOptions.equalsIgnoreCase(UIDriver.getExpectedElementValue("filterBy")), "psHomePageValidation",
                "Validate filter Types are displayed",
                "Filter types matches the expected list",
                "Filter types does not match expected values; Expected: " + UIDriver.getExpectedElementValue("filterBy") + " Actual: " + strFilterOptions);
        for (String filterGroup : FilterType) {
            String filterItemListInit = UIDriver.getElementProperty("filterListBySection");
            String filterItemList = filterItemListInit.replaceAll("SectionPlaceHolder", filterGroup);
            List<WebElement> filterList = UIDriver.getElementsByProperty(filterItemList);
            CSVReporter.reportPassFail(filterList.size() != 0, "psHomePageValidation",
                    "Validate filter items are displayed under " + filterGroup,
                    "Filter items are displayed",
                    "Filter items are not displayed");
        }
    }

    /**
     * @name ccLogout
     * @author Sujata Padhi
     * @description
     * @preCondition User is logged in into the application
     * @lastChanged
     * @TODO
     */

    @Test
    @Parameters({"DataRowID"})
    public static void psLogout(@Optional String DataRowID) {
        try {
            UIDriver.clickElement("userNameonHeader");
            UIDriver.clickElement("logoutLink");
            UIDriver.wait(5);
            CSVReporter.reportPassFail(UIDriver.checkElementpresent("Login", 3), "psLogout",
                    "Logout of aplication",
                    "Logout was successful",
                    "Logout was not successful");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * @name: psCreateProjectStatement
     * @author :Sujata padhi
     * @description: Create the project statement and validate the mandatory field(set user data as Random
     *               to select random compensation structure, else specify the compensation structure needed)
     * @preCondition : User needs to login with a valid Marketer user
     * @lastChanged
     * @TODO
     */

    @Test
    @Parameters({"DataRowID"})

    public static void psCreateProjectStatementPage1(@Optional String DataRowID) {

        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<String, String> input = Framework.getDataRecord(DataRowID, componentName);
        String strAgencyName = input.get("data");
        List<String> userDataAsList = Arrays.asList(input.get("userData").split("\\|"));
        String agencyLeadName = userDataAsList.get(0);
        String strBrandName = userDataAsList.get(2);
        boolean bPass = false;
        Calendar cal = Calendar.getInstance();

        UIDriver.clickElement("createNewButton");
        Assert.assertEquals(UIDriver.checkElementpresent("createNewButton", 3), true, "create new button not present");
        bPass = UIDriver.checkElementpresent("projectStatement", 3);

        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                "Create Project Statement link present", "Create Project Statement link present on the list",
                "Create Project Statement link not present");
        if (bPass) {

            UIDriver.clickElement("projectStatement");
        }
        UIDriver.wait(3);

        bPass = UIDriver.checkElementpresent("projectStatementHeader", 3)
                && UIDriver.getElementText("projectStatementHeader").length() > 0;
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                "Create new project statement page should displayed with header", "Create new project statement page is displayed",
                "Create new project statement page is not being displayed");

        //Project Name
        String strCurrentDateTime = Utility.getCurrentDate();
        String projectName = input.get("searchValue") + "_" + strCurrentDateTime;
        UIDriver.setValue("projectNameInputBox", projectName);
        PSToolUtility.projectStatementDataMap.put("ProjectName", projectName);

        //Agency
        bPass = UIDriver.checkElementpresent("agencyInputBox", 1);
        UIDriver.setValue("agencyInputBox", strAgencyName);
        String selectAgencyNameInit = UIDriver.getElementProperty("agencyName");
        String selectAgencyName = selectAgencyNameInit.replaceAll("AgencyPlaceholder", strAgencyName);
        UIDriver.clickElementWithoutProperty(selectAgencyName);
        PSToolUtility.projectStatementDataMap.put("Agency", strAgencyName);

        //Agency Lead
        bPass = UIDriver.checkElementpresent("agencyProjectLeadBox", 3);
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                "Agency Lead list must be shown", "Agency Lead list field present",
                "Agency Lead list field not present");
        UIDriver.selectValue("agencyProjectLeadBox", agencyLeadName, "text");
        PSToolUtility.projectStatementDataMap.put("Agency Project Lead", agencyLeadName);

        //Start date
        bPass = UIDriver.checkElementpresent("startDatePicker", 3);
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                "Start Date field must be shown", "Start Date field present",
                "Start Date field not present");
        UIDriver.clickElement("startDatePicker");

        String dateSelected = StringUtil.leftPad(UIDriver.getElementText("firstEnabledDate"), 2, "0")
                + " " + new SimpleDateFormat("MMM").format(cal.getTime())
                + ". " + new SimpleDateFormat("YYYY").format(cal.getTime());
        PSToolUtility.projectStatementDataMap.put("Project Start Date", dateSelected);
        UIDriver.clickElement("firstEnabledDate");


        //End date
        bPass = UIDriver.checkElementpresent("endDatePicker", 3);
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                "End Date field must be shown", "End Date field present",
                "End Date field not present");
        UIDriver.clickElement("endDatePicker");
        UIDriver.clickElement("nextMonth");
        UIDriver.clickElement("nextMonth");
        dateSelected = StringUtil.leftPad(UIDriver.getElementText("endDate"), 2, "0")
                + " " + new SimpleDateFormat("MMM").format(cal.getTime())
                + ". " + new SimpleDateFormat("YYYY").format(cal.getTime());
        PSToolUtility.projectStatementDataMap.put("Project End Date", dateSelected);
        UIDriver.clickElement("endDate");

        //Project Summary
        bPass = UIDriver.checkElementpresent("projectSummaryBox", 1);
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                "Project Summary Box field must be shown", "Project Summary Box field present",
                "Project Summary Box field not present");
        UIDriver.clickElement("projectSummaryBox");
        String projectSummary = Utility.randomTextGenerator(200);
        UIDriver.setValue("projectSummaryBox", projectSummary);
        PSToolUtility.projectStatementDataMap.put("Project Summary", projectSummary);

        //Compensation Structure
        if(userDataAsList.get(1).equalsIgnoreCase("RANDOM")) {
            List<String> compensationFeeList = UIDriver.getSelectValues("compensationStructureBox");
            Integer ind = new Random().nextInt(compensationFeeList.size()-1) + 1;
            compensationFee = compensationFeeList.get(ind);
        }
        else{
            compensationFee = userDataAsList.get(1);
        }
        bPass = UIDriver.checkElementpresent("compensationStructureBox", 1);
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                "Compensation Structure list must be shown", "Compensation Structure list field present",
                "Compensation Structure list field not present");
        UIDriver.selectValue("compensationStructureBox", compensationFee, "text");
        PSToolUtility.projectStatementDataMap.put("Compensation Structure", compensationFee);

        //Currency
        bPass = UIDriver.checkElementpresent("currencyList", 1);
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                "Currency list must be shown", "Currency list field present",
                "Currency list field not present");
        UIDriver.selectValue("currencyList",
                Integer.toString(new Random().nextInt(UIDriver.getSelectValues("currencyList").size())), "index");

        //Brand
        bPass = UIDriver.checkElementpresent("brandInputBox", 1);
        UIDriver.setValue("brandInputBox", strBrandName);
        String brandNameInit = UIDriver.getElementProperty("brandName");
        String brandName = brandNameInit.replaceAll("BrandPlaceholder", strBrandName);
        UIDriver.clickElementWithoutProperty(brandName);
        PSToolUtility.projectStatementDataMap.put("Brand", strBrandName);

        //Charter
        bPass = UIDriver.checkElementpresent("radioYesNo", 1);
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                "Yes/No radio button must be shown", "Yes/No radio button field present",
                "Yes/No radio button field not present");
        List<WebElement> yesNo = UIDriver.getElements("radioYesNo");
        WebElement selectedYesNo = yesNo.get(new Random().nextInt(yesNo.size()));
        selectedYesNo.click();
        PSToolUtility.projectStatementDataMap.put("Cluster Charter", selectedYesNo.getText());

        //Geographic scope
        bPass = UIDriver.checkElementpresent("geographicList", 1);
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                "Geographic list must be shown", "Geographic list field present",
                "Geographic list field not present");
        UIDriver.selectValue("geographicList",
                Integer.toString(new Random().nextInt(UIDriver.getSelectValues("geographicList").size())), "index");

        //Next page
        bPass = UIDriver.checkElementpresent("nextButton", 1);
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                "Next Button must be shown", "Next button is present",
                "Next button is not present");
        UIDriver.clickElement("nextButton");
        UIDriver.wait(2);
    }
    /**
     * @name: psCreateProjectStatement2
     * @author :Sujata padhi
     * @description:    Create the project statement page2. this component can be called using "SelectAll" to select all capabilities,
     *                  and use "Random" to choose random few capabilities
     * @preCondition : User need to be in page 2 afterr completing page1
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void psCreateProjectStatementPage2(@Optional String DataRowID) {
        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<String, String> input = Framework.getDataRecord(DataRowID, componentName);
        PSToolUtility.selectedCapabilityList = new ArrayList<String>();

        String capabilitySelection = input.get("data");
        boolean bPass = false;
        //Page 2 Select Capabilities
        if(capabilitySelection.equalsIgnoreCase("SelectAll")) {
            bPass = UIDriver.checkElementpresent("selectAllCheckBox", 2);
            CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage2",
                    "Select All CheckBox must be shown", "Select All CheckBox is present",
                    "Select All CheckBox is not present");
            UIDriver.clickElement("selectAllCheckBox");

            //Add to global list for future validation
            //int rndNbr = 29;
            List<WebElement> capabilityList = UIDriver.getElements("capabilityList");
            int rndNbr = capabilityList.size() - 1;
            for(int cnt = 0; cnt<=rndNbr; cnt++){
                WebElement capability = capabilityList.get(cnt);
                if(!PSToolUtility.selectedCapabilityList.contains(capability.getText())){
                    PSToolUtility.selectedCapabilityList.add(capability.getText());
                    System.out.println(capability.getText());
                }

            }
        }
        else {
            // there are 29 capabilities
            //int rndNbr = new Random().nextInt(29);
            bPass = UIDriver.checkElementpresent("capabilityList", 2);

            List<WebElement> capabilityList = UIDriver.getElements("capabilityList");
            int rndNbr = new Random().nextInt(capabilityList.size());
            for(int cnt = 0; cnt<rndNbr; cnt++){
                WebElement capability = capabilityList.get(new Random().nextInt(capabilityList.size() - 1));
                if(!PSToolUtility.selectedCapabilityList.contains(capability.getText())){
                    PSToolUtility.selectedCapabilityList.add(capability.getText());
                    System.out.println(capability.getText());
                    capability.click();
                }
            }
        }

        //Page2 Click on Create button
        bPass = UIDriver.checkElementpresent("createButtonPage2", 2);
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage2",
                "Create Button must be shown", "Create Button is present",
                "Create Button is not present");
        UIDriver.wait(1);
        UIDriver.clickElement("createButtonPage2");
    }

    /**
     * @name: psCreateProjectStatementPage3
     * @author :Sujata padhi
     * @description: Create the project statement page3
     * @preCondition : User need to be in page 3 after completing page2
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void psCreateProjectStatementPage3(@Optional String DataRowID) {
        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<String, String> input = Framework.getDataRecord(DataRowID, componentName);
        boolean bPass = false;
        String actionButton = input.get("data");

        //Page3 Click on Send button
        if(actionButton.equalsIgnoreCase("Send"))
        {
            bPass = UIDriver.checkElementpresent("sendButtonPage3", 1);
            UIDriver.clickElement("sendButtonPage3");
            UIDriver.wait(2);
            String projectName = PSToolUtility.projectStatementDataMap.get("ProjectName");
            String projectNameInit = UIDriver.getElementProperty("projectStatementByName");
            String projectNameProperty = projectNameInit.replaceAll("ProjectStmtPlaceHolder", projectName);
            UIDriver.wait(3);
            bPass = UIDriver.checkElementPresentWithoutProperty(projectNameProperty);
            CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage3",
                    "Project statement link must be shown", "Project statement link is present",
                    "Project statement link not present");
            UIDriver.clickElementWithoutProperty(projectNameProperty);

            //PS details should be shown
            bPass = UIDriver.checkElementpresent("reviewHeader", 1);
            CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage3",
                    "Project Statement details page should be shown",
                    "Project Statement details page is shown",
                    "Project Statement details page is NOT shown");
            //validate Showmore icon
            bPass = UIDriver.checkElementpresent("reviewShowMore", 1);
            CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage3",
                    "Show more icon should be shown",
                    "Show more icon is shown",
                    "Show more icon is NOT shown");
        }
        else {
            //Page3 Click on Continue button
            bPass = UIDriver.checkElementpresent("continueButtonPage3", 1);
            UIDriver.clickElement("continueButtonPage3");
        }
        CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage3",
                actionButton + " Button must be shown", actionButton + " Button is present",
                actionButton + " Button is not present");
    }


    /**
     * @name: psValidateProjectStatement
     * @author :Sujata padhi
     * @description: Validate project statement against Agency user login
     * @preCondition : New project statement is assigned to the agency to be completed
     * @lastChanged
     * @TODO
     */

    @Test
    @Parameters({"DataRowID"})
    public static void psValidateProjectStatement(@Optional String DataRowID) {

        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<String, String> input = Framework.getDataRecord(DataRowID, componentName);
        boolean bPass = false;
        String projectName = PSToolUtility.projectStatementDataMap.get("ProjectName");

        String projectNameInit = UIDriver.getElementProperty("projectStatementByName");
        String projectNameProperty = projectNameInit.replaceAll("ProjectStmtPlaceHolder", projectName);
        UIDriver.wait(3);
        bPass = UIDriver.checkElementPresentWithoutProperty(projectNameProperty);
        CSVReporter.reportPassFail(bPass, "psValidateProjectStatement",
                "Project statement link must be shown", "Project statement link is present",
                "Project statement link not present");
        UIDriver.clickElementWithoutProperty(projectNameProperty);
        try {
            bPass = PSToolUtility.validateProjectStmtData("Agency");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(bPass, true, "Data enter by Marketer is not same as what is shown to the Agency");
        CSVReporter.reportPassFail(bPass, "psValidateProjectStatement",
                "Data enter by Marketer should be same as what is shown to the Agency",
                "Data enter by Marketer is same as what is shown to the Agency",
                "Data enter by Marketer is not same as what is shown to the Agency");
    }

    /**
     * @name: psValidateProjectStatementWithOverView
     * @author :Sujata padhi
     * @description: Validate project statement against overview
     * @preCondition : user is in Overview page after completing new project statement
     * @lastChanged
     * @TODO
     */

    @Test
    @Parameters({"DataRowID"})
    public static void psValidateProjectStatementWithOverView(@Optional String DataRowID) {

        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        boolean bPass = false;

        try {
            bPass = PSToolUtility.validateProjectStmtData("Marketer");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(bPass, true, "Data enter by Marketer is not same as what is shown in Overview page");
        CSVReporter.reportPassFail(bPass, "psValidateProjectStatementWithOverView",
                "Data enter by Marketer should be same as what is shown in Overview page",
                "Data enter by Marketer is same as what is shown in Overview page",
                "Data enter by Marketer is not same as what is shown in Overview page");

        bPass = UIDriver.checkElementpresent("overviewNext", 1);
        Assert.assertEquals(bPass, true, "Next Button is not present");
        CSVReporter.reportPassFail(bPass, "psValidateProjectStatementWithOverView",
                "Next Button must be shown", "Next Button is present",
                "Next Button is not present");
        UIDriver.clickElement("overviewNext");
    }
    /**
     * @name: psDeliverables
     * @author :Sujata padhi
     * @description: Add deliverables for selected capabilities
     * @preCondition : User is in deliverables page
     * @lastChanged
     * @TODO
     */

    @Test
    @Parameters({"DataRowID"})
    public static void psDeliverables(@Optional String DataRowID) {

        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        boolean bPass = false;
        //check for deliverable page Next Button
        UIDriver.wait(1);
        bPass = UIDriver.checkElementpresent("deliverableNextButton", 1);
        Assert.assertEquals(bPass, true, "Footer Save & Next button is NOT shown");
        CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                "Footer Save & Next button should be shown",
                "Footer Save & Next button is shown",
                "Footer Save & Next button is NOT shown");
        UIDriver.clickElement("deliverableNextButton");
        UIDriver.wait(1);
        BrowserDriver.driver.switchTo().alert().accept();
        CSVReporter.reportPassFail(true, "psDeliverables",
                "Add Deliverables required message should be shown",
                "Add Deliverables  required message is shown",
                "Add Deliverables  required message is NOT shown");
        try {
            bPass = PSToolUtility.validateProjectStmtData("Deliverable");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(bPass, true, "Data enter by Marketer is not same as what is shown in Deliverable page header");
        CSVReporter.reportPassFail(bPass, "psDeliverables",
                "Data enter by Marketer should be same as what is shown in Deliverable page header",
                "Data enter by Marketer is same as what is shown in Deliverable page header",
                "Data enter by Marketer is not same as what is shown in Deliverable page header");

        List<String> deliverablesList = Utility.getElementText("deliverableCapabilityList");
        bPass = PSToolUtility.selectedCapabilityList.containsAll(deliverablesList);

        Assert.assertEquals(bPass, true, "All capalities selected in project statement is not shown");
        CSVReporter.reportPassFail(bPass, "psDeliverables",
                "All capalities selected in project statement should be shown",
                "All capalities selected in project statement shown",
                "All capalities selected in project statement is not shown");

        List<WebElement> deliverablesActionList = UIDriver.getElements("deliverableCapabilityActionList");
        int rnd = new Random().nextInt(deliverablesActionList.size());
        capabilityName = deliverablesList.get(rnd);
        deliverablesActionList.get(rnd).click();
        UIDriver.wait(2);
        System.out.println("Capability Name: " + capabilityName);
    }
    /**
     * @name: psAddDeliverables
     * @author :Sujata padhi
     * @description: Add deliverables for selected capabilities
     * @preCondition : User is in add deliverables page
     * @lastChanged
     * @TODO
     */

    @Test
    @Parameters({"DataRowID"})
    public static void psAddDeliverables(@Optional String DataRowID) {

        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        boolean bPass = false;

        bPass = UIDriver.checkElementpresent("addDeliverablesHeader", 1);
        CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                "Deliverable header should be shown",
                "Deliverable header is shown",
                "Deliverable header is NOT shown");
        // check for Back to List of Capabilities link
        bPass = UIDriver.checkElementpresent("addDeliverablesBackToCapability", 1);
        CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                "Back to List of Capabilities should be shown",
                "Back to List of Capabilities is shown",
                "Back to List of Capabilities is NOT shown");
        // verify if the header matches the selected Capability or not
        bPass = UIDriver.getElementText("addDeliverablesHeader").equals(capabilityName);
        CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                "Deliverable header should be same as deliverable selected from the list",
                "Deliverable header is same as deliverable selected from the list",
                "Deliverable header is NOT same as deliverable selected from the list");

        //Check for deliverable phases
        bPass = UIDriver.checkElementpresent("addDeliverablesPhasesList", 1);
        Assert.assertEquals(bPass, true, "Multiple phases tab are NOT shown");
        CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                "Multiple phases tab should be shown",
                "Multiple phases tab are shown",
                "Multiple phases tab are NOT shown");

        boolean duplicateDone = false;
        //Get the list of phases
        List<WebElement> phasesList = UIDriver.getElements("addDeliverablesPhasesList");
        for(WebElement phase : phasesList){
            System.out.println("Deliverable Phase : " + phase.getText());
            phase.click();
            UIDriver.wait(1);
            String activeTab = UIDriver.getElementAttribute("addDeliverableActiveTab", "data-attribute");
            String baseFee = Integer.toString(Utility.randomDigit());

            if(compensationFee.equalsIgnoreCase("Fixed Fee"))
            {
                // check for add fee link
                bPass = UIDriver.checkElementpresent("addDeliverablesAddFeeLink", 1);
                Assert.assertEquals(bPass, true, "Add Fee link is NOT shown");
                CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                        "Add Fee link should be shown",
                        "Add Fee link is shown",
                        "Add Fee link is NOT shown");
                UIDriver.clickElement("addDeliverablesAddFeeLink");

                //check for Base fee text box
                bPass = UIDriver.checkElementpresent("addDeliverablesBaseFeeTextBox", 1);
                Assert.assertEquals(bPass, true, "Base Fee textbox is NOT shown");
                CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                        "Base Fee textbox should be shown",
                        "Base Fee textbox is shown",
                        "Base Fee textbox is NOT shown");
                UIDriver.setValue("addDeliverablesBaseFeeTextBox", baseFee);

                //check for tick mark
                bPass = UIDriver.checkElementpresent("addDeliverablesSaveButton", 1);
                Assert.assertEquals(bPass, true, "Save button is NOT shown");
                CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                        "Save button should be shown",
                        "Save button is shown",
                        "Save button is NOT shown");
                UIDriver.clickElement("addDeliverablesSaveButton");
                UIDriver.wait(1);
                String phaseNameInit = UIDriver.getElementProperty("addDeliverablesFeesLabel");
                String phaseNameProperty = phaseNameInit.replaceAll("TabPlaceHolder", activeTab);

                bPass = UIDriver.checkElementPresentWithoutProperty(phaseNameProperty);
                CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                        "Fees for phase " + activeTab + " must be shown", "Fees for phase " + activeTab + " is shown",
                        "Fees for phase " + activeTab + " is NOT shown");

                //validate base fee added is same as shown on the right
                bPass = UIDriver.getElementByProperty(phaseNameProperty).getText().equals(baseFee);
                CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                        "Base fee added should be same as shown on the right",
                        "Base fee added is same as shown on the right",
                        "Base fee added is NOT same as shown on the right");
            }

            UIDriver.wait(1);

            //Select random deliverable from the list
            bPass = UIDriver.checkElementpresent("addDeliverablesPhaseCheckBoxList", 1);
            Assert.assertEquals(bPass, true, "No Deliverables found to add");
            CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                    "Deliverable list should be displayed",
                    "Deliverable list is displayed",
                    "No Deliverables found to add");
            List<WebElement> deliverableList = UIDriver.getElements("addDeliverablesPhaseCheckBoxList");
            int rnd = new Random().nextInt(deliverableList.size());
            WebElement deliverable = deliverableList.get(rnd);
            String deliverableTitle = UIDriver.getElements("addDeliverablesPhaseNameList").get(rnd).getText();
            System.out.println("Selected Deliverable: " + deliverableTitle);
            deliverable.click();
            UIDriver.wait(2);

            //check for deliverable name, only visible for "Other" deliverable
            if(deliverableTitle.equalsIgnoreCase("Other")) {
                bPass = UIDriver.checkElementpresent("addDeliverablesNameOfDeliverable", 1);
                Assert.assertEquals(bPass, true, "Deliverable Name textbox is NOT shown");
                CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                        "Deliverable Name textbox should be shown",
                        "Deliverable Name textbox is shown",
                        "Deliverable Name textbox is NOT shown");
                UIDriver.setValue("addDeliverablesNameOfDeliverable", Utility.randomTextGenerator(20));
            }

            //Deliverable details
            bPass = UIDriver.checkElementpresent("addDeliverablesAdditionalDetails", 1);
            Assert.assertEquals(bPass, true, "Additional Deliverable Details text area is NOT shown");
            CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                    "Additional Deliverable Details text area should be shown",
                    "Additional Deliverable Details text area is shown",
                    "Additional Deliverable Details text area is NOT shown");
            UIDriver.setValue("addDeliverablesAdditionalDetails", Utility.randomTextGenerator(100));

            // Enter fields for value based compensation structure
            if(compensationFee.equalsIgnoreCase("Value Based")) {
                //check for Base fee text box
                bPass = UIDriver.checkElementpresent("addDeliverablesBaseFee", 1);
                Assert.assertEquals(bPass, true, "Base Fee textbox is NOT shown");
                CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                        "Base Fee textbox should be shown",
                        "Base Fee textbox is shown",
                        "Base Fee textbox is NOT shown");
                UIDriver.setValue("addDeliverablesBaseFee", baseFee);

                //P4P Eligible
                bPass = UIDriver.checkElementpresent("addDeliverablesP4PEligibleYesNo", 1);
                CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                        "P4P Eligible Option must be shown", "P4P Eligible Option present",
                        "P4P Eligible Option not present");
                Integer ind = new Random().nextInt(UIDriver.getSelectValues("addDeliverablesP4PEligibleYesNo").size()-1) + 1;
                UIDriver.selectValue("addDeliverablesP4PEligibleYesNo", Integer.toString(ind), "index");

                if(ind == 1) {
                    //% P4P
                    bPass = UIDriver.checkElementpresent("addDeliverablesPercentP4P", 1);
                    CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                            "% P4P List must be shown", "% P4P List present",
                            "% P4P List not present");
                    ind = new Random().nextInt(UIDriver.getSelectValues("addDeliverablesPercentP4P").size()-1) +1;
                    UIDriver.selectValue("addDeliverablesPercentP4P", Integer.toString(ind), "index");
                }
            }
            //Expected Delivery date
            bPass = UIDriver.checkElementpresent("addDeliverablesDatePicker", 1);
            Assert.assertEquals(bPass, true, "Expected Delivery Date field not present");
            CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                    "Expected Delivery Date field must be shown",
                    "Expected Delivery Date field present",
                    "Expected Delivery Date field not present");
            UIDriver.clickElement("addDeliverablesDatePicker");
            UIDriver.clickElement("addDeliverablesDatePickerNextMonth");
            UIDriver.clickElement("firstEnabledDate");

            //check for footer Save Button
            bPass = UIDriver.checkElementpresent("addDeliverablesFooterSaveButton", 1);
            Assert.assertEquals(bPass, true, "Footer Save button is NOT shown");
            CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                    "Footer Save button should be shown",
                    "Footer Save button is shown",
                    "Footer Save button is NOT shown");
            UIDriver.clickElement("addDeliverablesFooterSaveButton");
            UIDriver.wait(1);

            if(new Random().nextBoolean() && !duplicateDone)
            {
                //check for Duplicate link
                bPass = UIDriver.checkElementpresent("addDeliverablesDuplicateLink", 1);
                Assert.assertEquals(bPass, true, "Duplicate link is NOT shown");
                CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                        "Duplicate link should be shown",
                        "Duplicate link is shown",
                        "Duplicate link is NOT shown");
                UIDriver.clickElement("addDeliverablesDuplicateLink");
                //Click Cancel
                BrowserDriver.driver.switchTo().alert().dismiss();
                CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                        "Click on Cancel",
                        "Clicked on Cancel",
                        "Could not Click on Cancel");
                UIDriver.wait(1);
                //Validate that nothing happened by checking number of Duplicate links. There should be only one
                CSVReporter.reportPassFail(UIDriver.getElements("addDeliverablesDuplicateLink").size() == 1, "psAddDeliverables",
                        "Verify that there is only one Duplicate link",
                        "Verified that there is only one Duplicate link",
                        "There are more than one Duplicate link");

                UIDriver.clickElement("addDeliverablesDuplicateLink");
                //Click Ok
                BrowserDriver.driver.switchTo().alert().accept();
                CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                        "Click on Ok",
                        "Clicked on Ok",
                        "Could not Click on Ok");
                UIDriver.wait(1);
                duplicateDone = true;
                deliverableCount = deliverableCount + 1;
                UIDriver.clickElement("addDeliverablesFooterSaveButton");
                CSVReporter.reportPassFail(UIDriver.getElements("addDeliverablesDuplicateLink").size() == 2, "psAddDeliverables",
                        "Verify that Duplicate deliverable is created",
                        "Duplicate deliverable is created",
                        "Duplicate deliverable is NOT created");
            }
        }
        //check for footer Save & Next Button
        bPass = UIDriver.checkElementpresent("addDeliverablesFooterSaveAndNextButton", 1);
        Assert.assertEquals(bPass, true, "Footer Save & Next button is NOT shown");
        CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                "Footer Save & Next button should be shown",
                "Footer Save & Next button is shown",
                "Footer Save & Next button is NOT shown");
        UIDriver.clickElement("addDeliverablesFooterSaveAndNextButton");
        UIDriver.wait(1);
    }
    /**
     * @name: psValidateAddedDeliverables
     * @author :Sujata padhi
     * @description: Validate number of deliverables added
     * @preCondition : User is in deliverables page after adding deliverables for a capability
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void psValidateAddedDeliverables(@Optional String DataRowID) {
        List<String> deliverablesList = Utility.getElementText("deliverableCapabilityList");
        List<WebElement> deliverablesActionList = UIDriver.getElements("deliverableCapabilityActionList");
        WebElement deliverableAction = deliverablesActionList.get(deliverablesList.indexOf(capabilityName));
        boolean bPass = deliverableAction.getText().equalsIgnoreCase(Integer.toString(deliverableCount) + " deliverable/s");
        Assert.assertEquals(bPass, true, "Number of deliverables added is NOT same as displayed");
        CSVReporter.reportPassFail(bPass, "psAddDeliverables",
                "Number of deliverables added should be same as displayed",
                "Number of deliverables added is same as displayed",
                "Number of deliverables added is NOT same as displayed");
        UIDriver.clickElement("deliverableNextButton");
        UIDriver.wait(1);
    }
     /**
     * @name: psAddRateCard
     * @author :Sujata padhi
     * @description: Select rate card for current project if the compensation structure is Hourly
     * @preCondition : User is in Rates page
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void psAddRateCard(@Optional String DataRowID) {
        boolean bPass;
        bPass = UIDriver.checkElementpresent("progressBarRates", 1);
        if(compensationFee.equalsIgnoreCase("Hourly Rate Card (Specific to vendor & capability)")){
            Assert.assertEquals(bPass, true, "Page to select Rate card is NOT available");
            CSVReporter.reportPassFail(bPass, "psAddRateCard",
                    "Page to select Rate card should be available",
                    "Page to select Rate card is available",
                    "Page to select Rate card is NOT available");
            List<WebElement> rateCardList = UIDriver.getElements("rateCardList");
            Integer ind = new Random().nextInt(rateCardList.size());
            rateCardList.get(ind).click();

            PSToolUtility.storePositionHourlyRate();
            UIDriver.clickElement("deliverableNextButton");
            UIDriver.wait(1);
            bPass = PSToolUtility.validatePositionHourlyRate();
            Assert.assertEquals(bPass, true, "Rates on selected rate card are NOT same as displayed on staffing page");
            CSVReporter.reportPassFail(bPass, "psAddRateCard",
                    "Rates on selected rate card should be same as displayed on staffing page",
                    "Rates on selected rate card are same as displayed on staffing page",
                    "Rates on selected rate card are NOT same as displayed on staffing page");

        }
        else{
            Assert.assertEquals(!bPass, true, "Page to select Rate card is available");
            CSVReporter.reportPassFail(!bPass, "psAddRateCard",
                    "Page to select Rate card should NOT be available",
                    "Page to select Rate card is NOT available",
                    "Page to select Rate card is available");
        }
    }
    /**
     * @name: psStaffing
     * @author :Sujata padhi
     * @description: Add staff  to the staffing plan
     * @preCondition : User is in Staffing page
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void psStaffing(@Optional String DataRowID) {
        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<String, String> input = Framework.getDataRecord(DataRowID, componentName);
        PSToolUtility.selectedPhasesList = new ArrayList<String>();
        String phasesOfWorkSelection = input.get("data");
        boolean bPass = false;

        //Next button
        bPass = UIDriver.checkElementpresent("staffingNext", 1);
        Assert.assertEquals(bPass, true, "Next button is NOT shown");
        CSVReporter.reportPassFail(bPass, "psStaffing",
                "Next button should be shown",
                "Next button is shown",
                "Next button is NOT shown");

        //validate no staffing data
        if(compensationFee.equalsIgnoreCase("Hourly Rate Card (Specific to vendor & capability)")){
            UIDriver.clickElement("staffingNext");
            BrowserDriver.driver.switchTo().alert().accept();
            CSVReporter.reportPassFail(true, "psStaffing",
                    "Staffing plan required message should be shown",
                    "Staffing plan required message is shown",
                    "Staffing plan required message is NOT shown");
            /*
            Alert alert = BrowserDriver.driver.switchTo().alert();
            System.out.println(alert.getText());
            UIDriver.wait(1);
            bPass =  alert.getText().equalsIgnoreCase("Staffing Plan is required for this project. Please identify staffing plan in order to proceed.");
            CSVReporter.reportPassFail(bPass, "psStaffing",
                    "Staffing plan required message should be shown",
                    "Staffing plan required message is shown",
                    "Staffing plan required message is NOT shown");
            alert.accept();
            */
        }

        // Click on the Add Staff link
        bPass = UIDriver.checkElementpresent("addStaffLink", 1);
        Assert.assertEquals(bPass, true, "Add Staff link is NOT shown");
        CSVReporter.reportPassFail(bPass, "psStaffing",
                "Add Staff link should be shown",
                "Add Staff link is shown",
                "Add Staff link is NOT shown");
        UIDriver.clickElement("addStaffLink");

        //check for Name text box
        bPass = UIDriver.checkElementpresent("addStaffNameTextBox", 1);
        Assert.assertEquals(bPass, true, "Name textbox is NOT shown");
        CSVReporter.reportPassFail(bPass, "psStaffing",
                "Name textbox should be shown",
                "Name textbox is shown",
                "Name textbox is NOT shown");
        String name = Utility.randomTextGenerator(15);
        UIDriver.setValue("addStaffNameTextBox", name);

        //Position text box
        String position;
        if(compensationFee.equalsIgnoreCase("Hourly Rate Card (Specific to vendor & capability)")){
            bPass = UIDriver.checkElementpresent("addStaffPositionTextBox", 3);
            CSVReporter.reportPassFail(bPass, "psStaffing",
                    "Position textbox must be shown",
                    "Position textbox present",
                    "Position textbox not present");
            List<String> positionList = Utility.getElementText("staffingPositionList");
            position = positionList.get(new Random().nextInt(positionList.size()));
            UIDriver.setValue("addStaffPositionTextBox", position);
            UIDriver.wait(3);
            UIDriver.clickElement("addStaffingPositionSelection");
        } else {
            bPass = UIDriver.checkElementpresent("addStaffPositionOtherTextBox", 3);
            CSVReporter.reportPassFail(bPass, "psStaffing",
                    "Position textbox must be shown",
                    "Position textbox present",
                    "Position textbox not present");
            position = Utility.randomTextGenerator(15);
            UIDriver.setValue("addStaffPositionOtherTextBox", position);
        }


        //Subcontractor?
        bPass = UIDriver.checkElementpresent("addStaffSubContractorSelect", 1);
        CSVReporter.reportPassFail(bPass, "psStaffing",
                "Is Employee subcontractor Option must be shown",
                "Is Employee subcontractor Option present",
                "Is Employee subcontractor Option not present");
        Integer ind = new Random().nextInt(UIDriver.getSelectValues("addStaffSubContractorSelect").size()-1) + 1;
        UIDriver.selectValue("addStaffSubContractorSelect", Integer.toString(ind), "index");

        if(ind == 1) {
            //Company supplying Contractor
            bPass = UIDriver.checkElementpresent("addStaffContractorSupplyCompanyTextBox", 1);
            CSVReporter.reportPassFail(bPass, "psStaffing",
                    "Company supplying Contractor textbox must be shown",
                    "Company supplying Contractor textbox present",
                    "Company supplying Contractor textbox not present");
            UIDriver.setValue("addStaffContractorSupplyCompanyTextBox", Utility.randomTextGenerator(15));
        }

        if(phasesOfWorkSelection.equalsIgnoreCase("SelectAll")) {
            bPass = UIDriver.checkElementpresent("selectAllCheckBox", 2);
            CSVReporter.reportPassFail(bPass, "psStaffing",
                    "Select All CheckBox must be shown", "Select All CheckBox is present",
                    "Select All CheckBox is not present");
            UIDriver.clickElement("selectAllCheckBox");

            //Add to global list for future validation
            List<WebElement> phasesList = UIDriver.getElements("addStaffingPhasesList");
            for(int cnt = 0; cnt<phasesList.size(); cnt++){
                WebElement phase = phasesList.get(cnt);
                if(!PSToolUtility.selectedPhasesList.contains(phase.getText())){
                    PSToolUtility.selectedPhasesList.add(phase.getText());
                    System.out.println(phase.getText());
                }

            }
        } else {
            bPass = UIDriver.checkElementpresent("addStaffingPhasesList", 2);
            List<WebElement> phasesList = UIDriver.getElements("addStaffingPhasesList");
            int rndNbr = new Random().nextInt(phasesList.size());
            for(int cnt = 0; cnt<=rndNbr; cnt++){
                WebElement phase = phasesList.get(new Random().nextInt(phasesList.size()));
                if(!PSToolUtility.selectedPhasesList.contains(phase.getText())){
                    PSToolUtility.selectedPhasesList.add(phase.getText());
                    System.out.println(phase.getText());
                    phase.click();
                }
            }
        }

        // Hours
        if(compensationFee.equalsIgnoreCase("Hourly Rate Card (Specific to vendor & capability)")) {
            bPass = UIDriver.checkElementpresent("addStaffingHoursTextBox", 1);
            Assert.assertEquals(bPass, true, "Hours textbox is NOT shown");
            CSVReporter.reportPassFail(bPass, "psStaffing",
                    "Hours textbox should be shown",
                    "Hours textbox is shown",
                    "Hours textbox is NOT shown");
            UIDriver.setValue("addStaffingHoursTextBox", Utility.randomNumberGenerator());
        }

        //Click on Save button
        bPass = UIDriver.checkElementpresent("addStaffingSaveButton", 2);
        CSVReporter.reportPassFail(bPass, "psStaffing",
                "Save Button must be shown", "Save Button is present",
                "Save Button is not present");
        UIDriver.clickElement("addStaffingSaveButton");
        UIDriver.wait(2);

        CSVReporter.reportPassFail(UIDriver.getElementText("staffingPosition").equalsIgnoreCase(position), "psStaffing"
                ,"Position value should be same as entered"
                ,"Position value is same as entered"
                ,"Position value is not same as entered");

        bPass = UIDriver.getElementText("staffingPhaseCount").equals(Integer.toString(PSToolUtility.selectedPhasesList.size()));
        CSVReporter.reportPassFail(bPass, "psStaffing"
                ,"Number of phases selected should be same as entered"
                ,"Number of phases selected is same as entered"
                ,"Number of phases selected is not same as entered");

        CSVReporter.reportPassFail(UIDriver.checkElementpresent("staffingRemove", 1), "psStaffing"
                ,"Remove link should be present"
                ,"Remove link is present"
                ,"Remove link is not present");

        UIDriver.clickElement("staffingNext");
        UIDriver.wait(2);
    }
    /**
     * @name: psExpenses
     * @author :Sujata padhi
     * @description: Add expenses
     * @preCondition : User is in Expenses page
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void psExpenses(@Optional String DataRowID) {

        boolean bPass;
        //Check for Travel expenses header
        bPass = UIDriver.checkElementpresent("expensesTravelHeader", 1);
        CSVReporter.reportPassFail(bPass, "psExpenses",
                "Travel Expenses header should be shown",
                "Travel Expenses header is shown",
                "Travel Expenses header is NOT shown");
        //Travel Cost
        bPass = UIDriver.checkElementpresent("expensesTravelCost", 1);
        CSVReporter.reportPassFail(bPass, "psExpenses",
                "Travel Cost textbox should be shown",
                "Travel Cost textbox is shown",
                "Travel Cost textbox is NOT shown");
        UIDriver.setValue("expensesTravelCost", Integer.toString(Utility.randomDigit()));
        //Cities of Travel
        bPass = UIDriver.checkElementpresent("expensesCitiesOfTravel", 1);
        CSVReporter.reportPassFail(bPass, "psExpenses",
                "Cities of Travel textbox should be shown",
                "Cities of Travel textbox is shown",
                "Cities of Travel textbox is NOT shown");
        UIDriver.setValue("expensesCitiesOfTravel", Utility.randomTextGenerator(20));
        //# Trips
        bPass = UIDriver.checkElementpresent("expensesNbrOfTrips", 1);
        CSVReporter.reportPassFail(bPass, "psExpenses",
                "# Trips textbox should be shown",
                "# Trips textbox is shown",
                "# Trips textbox is NOT shown");
        UIDriver.setValue("expensesNbrOfTrips", Integer.toString(Utility.randomDigit()));
        //# Travelers
        bPass = UIDriver.checkElementpresent("expensesNbrOfTravelers", 1);
        CSVReporter.reportPassFail(bPass, "psExpenses",
                "# Travelers textbox should be shown",
                "# Travelers textbox is shown",
                "# Travelers textbox is NOT shown");
        UIDriver.setValue("expensesNbrOfTravelers", Integer.toString(Utility.randomDigit()));
        //Business Purpose of Travel
        bPass = UIDriver.checkElementpresent("expensesBusinessPurpose", 1);
        CSVReporter.reportPassFail(bPass, "psExpenses",
                "Business Purpose of Travel textbox should be shown",
                "Business Purpose of Travel textbox is shown",
                "Business Purpose of Travel textbox is NOT shown");
        UIDriver.setValue("expensesBusinessPurpose", Utility.randomTextGenerator(100));

        //Check for Out of Pocket expenses header
        bPass = UIDriver.checkElementpresent("expensesOutOfPocketHeader", 1);
        CSVReporter.reportPassFail(bPass, "psExpenses",
                "Out of Pocket Expenses header should be shown",
                "Out of Pocket Expenses header is shown",
                "Out of Pocket Expenses header is NOT shown");
        //OOP Costs
        bPass = UIDriver.checkElementpresent("expensesOOPCost", 1);
        CSVReporter.reportPassFail(bPass, "psExpenses",
                "OOP Costs textbox should be shown",
                "OOP Costs textbox is shown",
                "OOP Costs textbox is NOT shown");
        UIDriver.setValue("expensesOOPCost", Integer.toString(Utility.randomDigit()));
        //OOP Details
        bPass = UIDriver.checkElementpresent("expensesOOPDetails", 1);
        CSVReporter.reportPassFail(bPass, "psExpenses",
                "OOP Details textbox should be shown",
                "OOP Details textbox is shown",
                "OOP Details textbox is NOT shown");
        UIDriver.setValue("expensesOOPDetails", Utility.randomTextGenerator(100));

        //Add Categories link only available for Hourly rate project
        /*
        if(compensationFee.equalsIgnoreCase("Hourly Rate Card (Specific to vendor & capability)")) {
            bPass = UIDriver.checkElementpresent("expensesAddCategories", 1);
            CSVReporter.reportPassFail(bPass, "psStaffing",
                    "Add Categories link should be shown",
                    "Add Categories link is shown",
                    "Add Categories link is NOT shown");
        }
        */
        //Review button
        bPass = UIDriver.checkElementpresent("expensesReviewButton", 1);
        Assert.assertTrue(bPass, "Review button is not present");
        CSVReporter.reportPassFail(bPass, "psStaffing",
                "Review Button should be shown",
                "Review Button is shown",
                "Review Button is NOT shown");
        UIDriver.clickElement("expensesReviewButton");
        UIDriver.wait(3);
    }
    /**
     * @name: psReview
     * @author :Sujata padhi
     * @description: Review entered summary
     * @preCondition : User is in Project statement review page
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void psReview(@Optional String DataRowID) {
        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<String, String> input = Framework.getDataRecord(DataRowID, componentName);

        String action = input.get("data");
        boolean bPass;
        //PS details should be shown
        bPass = UIDriver.checkElementpresent("reviewHeader", 1);
        CSVReporter.reportPassFail(bPass, "psReview",
                "Project Statement details page should be shown",
                "Project Statement details page is shown",
                "Project Statement details page is NOT shown");
        //validate Showmore icon
        bPass = UIDriver.checkElementpresent("reviewShowMore", 1);
        CSVReporter.reportPassFail(bPass, "psReview",
                "Show more icon should be shown",
                "Show more icon is shown",
                "Show more icon is NOT shown");
        try {
            bPass = PSToolUtility.validateProjectStmtData("MarketerReview");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(true, true, "Data enter by Marketer is not same as what is shown in review page");
        CSVReporter.reportPassFail(bPass, "psReview",
                "Data entered by Marketer should be same as what is shown in review page",
                "Data entered by Marketer is same as what is shown in review page",
                "Data entered by Marketer is not same as what is shown in review page");

        //Send button
        bPass = UIDriver.checkElementpresent("reviewSendButton", 1);
        Assert.assertTrue(bPass, "Send button is not present");
        CSVReporter.reportPassFail(bPass, "psReview",
                "Send Button should be shown",
                "Send Button is shown",
                "Send Button is NOT shown");

        if(action.equalsIgnoreCase("Send")) {
            UIDriver.clickElement("reviewSendButton");
        } else {
            UIDriver.clickElement("reviewShowMore");
            UIDriver.clickElement("reviewCancel");
            //Click Cancel
            BrowserDriver.driver.switchTo().alert().dismiss();
            CSVReporter.reportPassFail(bPass, "psReview",
                    "Click on Cancel",
                    "Clicked on Cancel",
                    "Could not Click on Cancel");
            UIDriver.wait(1);
            UIDriver.clickElement("reviewCancel");
            //Click Ok
            BrowserDriver.driver.switchTo().alert().accept();
            CSVReporter.reportPassFail(bPass, "psReview",
                    "Click on Ok",
                    "Clicked on Ok",
                    "Could not Click on Ok");
            UIDriver.wait(1);
            //Cancellation Reason
            bPass = UIDriver.checkElementpresent("reviewCancellationReasonTextbox", 1);
            CSVReporter.reportPassFail(bPass, "psReview",
                    "Cancellation reason textbox should be shown",
                    "Cancellation reason textbox is shown",
                    "Cancellation reason textbox is NOT shown");
            UIDriver.setValue("reviewCancellationReasonTextbox", Utility.randomTextGenerator(20));
            //Save button
            bPass = UIDriver.checkElementpresent("reviewCancellationReasonSaveButton", 1);
            CSVReporter.reportPassFail(bPass, "psReview",
                    "Save button should be shown",
                    "Save button is shown",
                    "Save button is NOT shown");
            UIDriver.clickElement("reviewCancellationReasonSaveButton");

            UIDriver.wait(2);

            //Verify PS is cancelled
            bPass = UIDriver.getElementText("reviewProjectStatus").equalsIgnoreCase("Cancelled");
            CSVReporter.reportPassFail(bPass, "psReview",
                    "Project Status should be shown as Cancelled",
                    "Project Status is shown as Cancelled",
                    "Project Status is NOT shown as cancelled");

        }

        UIDriver.wait(3);
    }

    /**
     * @name: psCreatePitchAgreementPage1
     * @author :Sujata padhi
     * @description: Create Pitch agreement and validate the mandatory field
     * @preCondition : User needs to login with a valid Marketer user  Credential
     * @lastChanged
     * @TODO
     */

    @Test
    @Parameters({"DataRowID"})

    public static void psCreatePitchAgreementPage1(@Optional String DataRowID) {

        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<String, String> input = Framework.getDataRecord(DataRowID, componentName);
        String strAgencyName = input.get("data");
        boolean bPass;

        bPass = UIDriver.checkElementpresent("createNewButton", 1);
        Assert.assertEquals(bPass, true, "CreateNewButton is not present");
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage1",
                "CreateNewButton should be present in home page",
                "createNewButton is present",
                "CreateNewButton is not present");

        UIDriver.clickElement("createNewButton");

        bPass = UIDriver.checkElementpresent("pitchAgreement", 1);
        Assert.assertEquals(bPass, true, "Create Pitch Agreement link not present");
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage1",
                "Create Pitch Agreement link present",
                "Create Pitch Agreement link present on the list",
                "Create Pitch Agreement link not present");
        UIDriver.clickElement("pitchAgreement");
        UIDriver.wait(1);

        bPass = UIDriver.checkElementpresent("pitchAgreementTitle", 1)
                && UIDriver.getElementText("pitchAgreementTitle").length() > 0;
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage1",
                "Create New Pitch Agreement page should be displayed with header",
                "Create New Pitch Agreement page is displayed",
                "Create New Pitch Agreement page is not being displayed");

        //Pitch Agreement Name
        String strCurrentDateTime = Utility.getCurrentDate();
        String pitchAgreementName = input.get("searchValue") + "_" + strCurrentDateTime;
        UIDriver.checkElementpresent("pitchAgreementInputBox", 2);
        UIDriver.setValue("pitchAgreementInputBox", pitchAgreementName);
        //PSToolUtility.projectStatementDataMap.put("ProjectName", pitchAgreementName);

        //Agency Box
        UIDriver.wait(2);
        bPass = UIDriver.checkElementpresent("agencyInputBox", 1);
        UIDriver.setValue("agencyInputBox", strAgencyName);

        //Company Name
        bPass = UIDriver.checkElementpresent("companyName", 3);
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage1",
                "Company name should dispalyed", "Company name is displaying",
                "Company name box not being displayed");
        //System.out.println("Company name is:"+ UIDriver.getElementText("companyName"));
        //Continue button and Cancel button validation

        bPass = UIDriver.checkElementpresent("pitchCancelButton", 2);
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage1",
                "Cancel button should be displayed", "Cancel button is displayed",
                "Cancel button is not displayed");
        bPass = UIDriver.checkElementpresent("pitchContinueButton", 2);
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage1",
                "Continue button should dispalyed", "Continue button is displaying",
                "Continue button is not present");
        UIDriver.clickElement("pitchContinueButton");

        //Page2 header validation

        UIDriver.wait(2);

        bPass = UIDriver.checkElementpresent("pitchPage2Header", 3)
                && UIDriver.getElementText("pitchPage2Header").length() > 0;
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage2",
                "Page2 should displayed with header", "Pitch agreement page2 is displayed",
                "Pitch agreement page2  is not being displayed");

        //Project Brief
        bPass = UIDriver.checkElementpresent("projectBriefInputBox", 1);
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage2",
                "Project Brief Box field must be shown", "Project Brief Box field present",
                "Project Brief Box field not present");
        String projectBrief = Utility.randomTextGenerator(200);
        UIDriver.setValue("projectBriefInputBox", projectBrief);

        //Fees and Expense checkbox
        bPass = UIDriver.checkElementpresent("feesandExpenseCheckBox", 1);
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage2",
                "Fees and Expenses Checkbox must be shown", "Fees and Expenses Checkbox is shown",
                "Fees and Expenses Checkbox is not shown");
        UIDriver.clickElement("feesandExpenseCheckBox");

        //Payment to Agency input Box
        bPass = UIDriver.checkElementpresent("paymentToAgency", 1);
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage2",
                "Payment To Agency field must be shown", "Payment To Agency field is shown",
                "Payment To Agency field is not shown");
        UIDriver.clickElement("paymentToAgency");
        String Expense = Utility.randomNumberGenerator();
        UIDriver.setValue("paymentToAgency", Expense);

        //Effective date
        bPass = UIDriver.checkElementpresent("effectiveDate", 1);
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage2",
                "Effective Date field must be shown", "Effective Date field is shown",
                "Effective Date field is not shown");
        UIDriver.clickElement("effectiveDate");
        UIDriver.clickElement("todayDate");


        //Expire date
        bPass = UIDriver.checkElementpresent("expirationDate", 1);
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage2",
                "Expire Date field must be shown", "Expire Date field present",
                "Expire Date field not present");
        UIDriver.clickElement("expirationDate");
        UIDriver.clickElement("nextMonth");
        UIDriver.clickElement("nextMonth");
        UIDriver.clickElement("endDate");

        //Start over
        bPass= UIDriver.checkElementpresent("page3StartOverButton",1);
        CSVReporter.reportPassFail(bPass,"psCreatePitchAgreementPage2",
                "Start Over button must be present", "Start Over button is present", "Start Over button is disabled");
        UIDriver.clickElement("page3StartOverButton");

        // Validate user is back to first page of Pitch Agreement
        bPass = UIDriver.checkElementpresent("pitchAgreementTitle", 1)
                && UIDriver.getElementText("pitchAgreementTitle").length() > 0;
        CSVReporter.reportPassFail(bPass, "psCreatePitchAgreementPage1",
                "Create New Pitch Agreement page should be displayed with header",
                "Create New Pitch Agreement page is displayed",
                "Create New Pitch Agreement page is not being displayed");
        UIDriver.clickElement("pitchContinueButton");

        //Continue button
        UIDriver.wait(1);
        bPass= UIDriver.checkElementpresent("continueButtonPitchPage2",1);
        CSVReporter.reportPassFail(bPass,"psCreatePitchAgreementPage2",
               "Continue button must be present", "Continue button is present", "Continue button is disabled");
        UIDriver.clickElement("continueButtonPitchPage2");
        // Page 3
        UIDriver.wait(1);
        //Add more recipients
        bPass= UIDriver.checkElementpresent("page3AddAdditionalRecipients",1);
        CSVReporter.reportPassFail(bPass,"psCreatePitchAgreementPage3",
                "Add Additional Recipients button must be present",
                "Add Additional Recipients button is present",
                "Add Additional Recipients button is disabled");
        UIDriver.clickElement("page3AddAdditionalRecipients");

        List<WebElement> recipientList = UIDriver.getElements("recipientRows");
        for(WebElement recipient: recipientList){
            String rowNumber = recipient.getAttribute("data-varindex");
            //Recipient Name
            String fieldInit = UIDriver.getElementProperty("page3NameTextBox");
            String fieldProperty = fieldInit.replaceAll("RowNumber", rowNumber);
            bPass = UIDriver.checkElementPresentWithoutProperty(fieldProperty);
            CSVReporter.reportPassFail(bPass,"psCreatePitchAgreementPage3",
                    "Recipient Name textbox must be present", "Recipient Name textbox is present",
                    "Recipient Name textbox is not present");
            UIDriver.setValueWithoutProperty(fieldProperty, Utility.randomTextGenerator(15));

            //Recepient Email
            fieldInit = UIDriver.getElementProperty("page3EmailTextBox");
            fieldProperty = fieldInit.replaceAll("RowNumber", rowNumber);
            bPass = UIDriver.checkElementPresentWithoutProperty(fieldProperty);
            CSVReporter.reportPassFail(bPass,"psCreatePitchAgreementPage3",
                    "Recipient Email textbox must be present", "Recipient Email textbox is present",
                    "Recipient Email textbox is not present");
            UIDriver.setValueWithoutProperty(fieldProperty, "test@test.com");

            //Recepient Title
            fieldInit = UIDriver.getElementProperty("page3TitleTextBox");
            fieldProperty = fieldInit.replaceAll("RowNumber", rowNumber);
            bPass = UIDriver.checkElementPresentWithoutProperty(fieldProperty);
            CSVReporter.reportPassFail(bPass,"psCreatePitchAgreementPage3",
                    "Recipient Title textbox must be present", "Recipient Title textbox is present",
                    "Recipient Title textbox is not present");
            UIDriver.setValueWithoutProperty(fieldProperty, Utility.randomTextGenerator(10));
        }
        // Check for Cancel and Send buttons
        bPass= UIDriver.checkElementpresent("page3CancelButton",1);
        CSVReporter.reportPassFail(bPass,"psCreatePitchAgreementPage3",
                "Cancel button must be present", "Cancel button is present", "Cancel button is not present");

        bPass= UIDriver.checkElementpresent("page3SendForSignatureButton",1);
        CSVReporter.reportPassFail(bPass,"psCreatePitchAgreementPage3",
                "Send to Signature button must be present", "Send to Signature button is present", "Send to Signature button is not present");
        UIDriver.clickElement("page3SendForSignatureButton");

        UIDriver.wait(2);
        //Close
        bPass= UIDriver.checkElementpresent("page3CloseButton",1);
        CSVReporter.reportPassFail(bPass,"psCreatePitchAgreementPage4",
                "Close button must be present", "Close button is present", "Close button is not present");

        UIDriver.clickElement("page3CloseButton");
    }


}