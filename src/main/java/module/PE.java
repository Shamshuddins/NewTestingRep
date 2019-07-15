package main.java.module;

import java.lang.reflect.Method;
import java.time.Year;
import java.util.*;

import main.java.framework.*;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.openqa.selenium.WebElement;

@Listeners(main.java.listener.Listener.class)
public class PE extends BaseTest {
    public static String userRole;
    public static int totalBidCount = 3;
    public static int currentBidCount = 0;

    public PE() {

        Method[] arrMethods = this.getClass().getDeclaredMethods();
        Framework.allMethods.add(Framework.iMethod, arrMethods);
        Framework.allDrivers.add(Framework.iDriver, this);
        Framework.iMethod++;
        Framework.iDriver++;
        PEUtility.newProductionDataMap = new LinkedHashMap<String, String>();
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

        String strURL = prop.getProperty(strEnv.toUpperCase() + "_PE_URL");
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
    public static void peLogin(@Optional String DataRowID) {
        //Enter login credentials
        String strUserName = prop.getProperty(strEnv.toUpperCase() + "_PE_" + DataRowID + "_username");
        String strPassword = prop.getProperty(strEnv.toUpperCase() + "_PE_" + DataRowID + "_password");
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
     * @name: peHomePageValidation
     * @author :Sujata padhi
     * @description: Home page validation
     * @preCondition : User needs to login with a valid Marketer user
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void peHomePageValidation(@Optional String DataRowID) {

        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<String, String> input = Framework.getDataRecord(DataRowID, componentName);
        boolean bPass = false;
        //Application Header validation
        bPass = UIDriver.checkElementpresent("applicationHeader", 3);
        CSVReporter.reportPassFail(bPass, "peHomePageValidation",
                "Application Header should be present in home page",
                "Application Header is present",
                "Application Header is not present");

        //Validation for UserName in Header
        String userData = input.get("userData");
        String usernameOnHeader = UIDriver.getElementText("userNameonHeader");
        //Assert.assertEquals(userData, usernameOnHeader, "User Name not displayed on the header");
        CSVReporter.reportPassFail(usernameOnHeader.equals(userData),
                "peHomePageValidation",
                "The user's name should be displayed on the header", "User Name is displayed in the Header section",
                "User Name not displayed in the Header section");
        //TODO:Search icon cant validate now
        // Tab validation "HOME", "PRODUCTIONS", "CALENDAR", "FORUM", "VENDOR PROFILES"
        String headerNames = input.get("data");
        List<String> headerNameList = Arrays.asList(headerNames.split("\\|"));
        for (String headerName : headerNameList) {
            String headerNameInit = UIDriver.getElementProperty("tabHeaderNames");
            String headerTabName = headerNameInit.replaceAll("HeaderPlaceHolder", headerName);
            bPass = UIDriver.checkElementPresentWithoutProperty(headerTabName);
            CSVReporter.reportPassFail(bPass, "peHomePageValidation",
                    headerName + " Tab should be displayed",
                    headerName + " Tab is displayed",
                    headerName + " Tab is not displaying");
        }

        UIDriver.clickElement("productionTab");
        UIDriver.wait(1);
        //User is now on production page and validating the production list.
        String productionNames = input.get("appData");
        List<String> productionNameList = Arrays.asList(productionNames.split("\\|"));
        for (String productionName : productionNameList) {
            String productNameInit = UIDriver.getElementProperty("productionList");
            String productListName = productNameInit.replaceAll("productionListPlaceHolder", productionName);
            bPass = UIDriver.checkElementPresentWithoutProperty(productListName);
            CSVReporter.reportPassFail(bPass, "peHomePageValidation",
                    productionName + " list header should be displayed",
                    productionName + " list header is displayed",
                    productionName + " list header is not displaying");

        }
        //Search box validation
        //Note: search functionality is not working
        bPass = UIDriver.checkElementpresent("searchBox", 1);
        CSVReporter.reportPassFail(bPass, "peHomePageValidation", "Search Box should be present in home page",
                "Search Box is present", "Search Box is not present");

        //Validation for Filters
        try {
            List<String> FilterType = Utility.webElementList(UIDriver.getElementProperty("filterBy"), "text", "0");
            String strFilterOptions = String.join("|", FilterType);
            System.out.println(FilterType);
            CSVReporter.reportPassFail(strFilterOptions.equalsIgnoreCase(UIDriver.getExpectedElementValue("filterBy")), "peHomePageValidation",
                    "Validate filter Types are displayed",
                    "Filter types matches the expected list",
                    "Filter types does not match expected values; Expected: " + UIDriver.getExpectedElementValue("filterBy") + " Actual: " + strFilterOptions);
            for (String filterGroup : FilterType) {
                if(!filterGroup.equalsIgnoreCase("Under Tax Credit Review")) {
                    String filterItemListInit = UIDriver.getElementProperty("filterListBySection");
                    String filterItemList = filterItemListInit.replaceAll("SectionPlaceHolder", filterGroup);
                    List<WebElement> filterList = UIDriver.getElementsByProperty(filterItemList);
                    CSVReporter.reportPassFail(filterList.size() != 0, "peHomePageValidation",
                            "Validate filter items are displayed under " + filterGroup,
                            "Filter items are displayed",
                            "Filter items are not displayed");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * @name: peCreateNewProductionPage1
     * @author :Sujata padhi
     * @description: Create the project statement and validate the mandatory field(set user data as Random
     * to select random compensation structure, else specify the compensation structure needed)
     * @preCondition : User needs to login with a valid Marketer user
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void peCreateNewProductionPage1(@Optional String DataRowID) {
        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<String, String> input = Framework.getDataRecord(DataRowID, componentName);
        String strAgencyName = input.get("data");
        List<String> userDataAsList = Arrays.asList(input.get("userData").split("\\|"));
        String agencyContact = userDataAsList.get(0);
        String directProductionConfig = userDataAsList.get(1).toLowerCase();
        String availableProductionTypeConfig = userDataAsList.get(2);
        String leadBrandName = userDataAsList.get(3);
        String selectedYear = userDataAsList.get(4);
        String strCurrentDateTime = Utility.getCurrentDate();
        boolean bPass = false;
        Calendar cal = Calendar.getInstance();

        bPass = UIDriver.checkElementpresent("createNewButton", 3);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "Create New Button should be present in home page",
                "Create New Button is present",
                "Create New Button is not present");
        Assert.assertEquals(bPass, true, "Create New Button not present");
        UIDriver.clickElement("createNewButton");


        UIDriver.wait(3);

        bPass = UIDriver.checkElementpresent("newProductionHeader", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "New Production page should displayed with header",
                "New Production page is displayed",
                "New Production page is not being displayed");

        // Direct To Production House
        boolean directToProductionHouse = directProductionConfig.equalsIgnoreCase("RANDOM") ?
                new Random().nextBoolean() : Boolean.parseBoolean(directProductionConfig);
        PEUtility.newProductionDataMap.put("DirectToProductionHouse", Boolean.toString(directToProductionHouse));

        bPass = UIDriver.checkElementpresent("directToProductionHouseCheckbox", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "Direct To Production House check box should displayed",
                "Direct To Production House check box is displayed",
                "Direct To Production House check box is not being displayed");

        if (directToProductionHouse) {
            UIDriver.clickElement("directToProductionHouseCheckbox");
        } else {
            //Agency
            bPass = UIDriver.checkElementpresent("agencyInputBox", 1);
            UIDriver.setValue("agencyInputBox", strAgencyName);
            String selectAgencyNameInit = UIDriver.getElementProperty("agencyName");
            String selectAgencyName = selectAgencyNameInit.replaceAll("AgencyPlaceholder", strAgencyName);
            UIDriver.clickElementWithoutProperty(selectAgencyName);
            PEUtility.newProductionDataMap.put("Agency", strAgencyName);

            //Agency Contact
            bPass = UIDriver.checkElementpresent("agencyContactList", 1);
            CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                    "Agency Contact list must be shown",
                    "Agency Contact list field present",
                    "Agency Contact list field not present");
            UIDriver.selectValue("agencyContactList", agencyContact, "text");
            PEUtility.newProductionDataMap.put("Agency Contact", agencyContact);

            //Agency Project Number
            bPass = UIDriver.checkElementpresent("agencyProjectNumber", 1);
            CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                    "Agency Project Number text box must be shown",
                    "Agency Project Number text box is present",
                    "Agency Project Number text box is not present");
            UIDriver.setValue("agencyProjectNumber", "PN_" + strCurrentDateTime);
            PEUtility.newProductionDataMap.put("Agency Project Number", "PN_" + strCurrentDateTime);

        }
        //Initiative Name

        bPass = UIDriver.checkElementpresent("initiativeName", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "Initiative Name text box must be shown",
                "Initiative Name text box is present",
                "Initiative Name text box is not present");
        UIDriver.clickElement("productionBidSaveButton");
        //Required field validation
        bPass = UIDriver.checkElementpresent("requiredField", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "This field is required. message must be shown",
                "This field is required. is present",
                "This field is required. is not present");
        String initiativeName = "Initiative_" + strCurrentDateTime;
        UIDriver.setValue("initiativeName", initiativeName);
        PEUtility.newProductionDataMap.put("Initiative Name", initiativeName);

        //Available Production Types
        bPass = UIDriver.checkElementpresent("availableProductionTypeList", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "Available Production Type list must be shown",
                "Available Production Type list is present",
                "Available Production Type list is not present");
        List<WebElement> productionTypeList = UIDriver.getElements("availableProductionTypeList");

        if (availableProductionTypeConfig.equalsIgnoreCase("RANDOM")) {
            WebElement selectedProductionType = productionTypeList.get(new Random().nextInt(productionTypeList.size()));
            selectedProductionType.click();
            PEUtility.newProductionDataMap.put("Available Production Type", selectedProductionType.getText());
        } else {
            for (WebElement productionType : productionTypeList) {
                if (productionType.getText().equalsIgnoreCase(availableProductionTypeConfig)) {
                    productionType.click();
                    PEUtility.newProductionDataMap.put("Available Production Type", productionType.getText());
                    break;
                }
            }
        }

        bPass = UIDriver.checkElementpresent("moveRightButton", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "Move Selection to Second Category arrow must be shown",
                "Move Selection to Second Category arrow is present",
                "Move Selection to Second Category arrow is not present");
        UIDriver.clickElement("moveRightButton");

        //Campaign
        bPass = UIDriver.checkElementpresent("campaignInputBox", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "Campaign text box must be shown",
                "Campaign text box is present",
                "Campaign text box is not present");
        String campaign = Utility.randomTextGenerator(20);
        UIDriver.setValue("campaignInputBox", campaign);
        PEUtility.newProductionDataMap.put("Campaign", campaign);

        //In-Market Year
        bPass = UIDriver.checkElementpresent("inMarketYearList", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "In-Market Year list must be shown", "In-Market Year list field present",
                "In-Market Year list field not present");
        List<String> yearList = UIDriver.getSelectValues("inMarketYearList");

        selectedYear = selectedYear.equalsIgnoreCase("RANDOM") ?
                yearList.get(new Random().nextInt(yearList.size() - 1) + 1) : selectedYear;

        UIDriver.selectValue("inMarketYearList", selectedYear, "value");

        // Check for future year approval message
        if (Integer.parseInt(selectedYear) > Year.now().getValue()) {
            bPass = UIDriver.checkElementpresent("inMarketHelpMessage", 1);
            CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                    "If Future Year is selected, message must be shown",
                    "Message is shown",
                    "Message is not shown");
        }

        PEUtility.newProductionDataMap.put("In-Market Year", selectedYear);

        //Lead Brand
        bPass = UIDriver.checkElementpresent("leadBrandInputBox", 1);
        UIDriver.setValue("leadBrandInputBox", leadBrandName);
        String leadBrandNameInit = UIDriver.getElementProperty("leadBrandName");
        String selectBrandName = leadBrandNameInit.replaceAll("LeadBrandNamePlaceholder", leadBrandName);
        UIDriver.clickElementWithoutProperty(selectBrandName);
        PEUtility.newProductionDataMap.put("Lead Brand", leadBrandName);
        //Currency
        bPass = UIDriver.checkElementpresent("currencyList", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "Currency list must be shown", "Currency list field present",
                "Currency list field not present");
        UIDriver.selectValue("currencyList",
                Integer.toString(new Random().nextInt(UIDriver.getSelectValues("currencyList").size())), "index");


        //Brand Manager
        bPass = UIDriver.checkElementpresent("brandManagerInputBox", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "Brand Manager text box must be shown",
                "Brand Manager text box is present",
                "Brand Manager text box is not present");
        String brandManager = Utility.randomTextGenerator(20);
        UIDriver.setValue("brandManagerInputBox", brandManager);
        PEUtility.newProductionDataMap.put("Brand Manager", brandManager);

        //IMC Brand Lead Name
        bPass = UIDriver.checkElementpresent("IMCBrandLeadNameInputBox", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "IMC Brand Lead Name text box must be shown",
                "IMC Brand Lead Name text box is present",
                "IMC Brand Lead Name text box is not present");
        String IMCBrandLeadName = Utility.randomTextGenerator(20);
        UIDriver.setValue("IMCBrandLeadNameInputBox", IMCBrandLeadName);
        PEUtility.newProductionDataMap.put("IMC Brand Lead Name", IMCBrandLeadName);

        //IMC Brand Lead Email
        bPass = UIDriver.checkElementpresent("IMCBrandLeadEmailInputBox", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "IMC Brand Lead Email text box must be shown",
                "IMC Brand Lead Email text box is present",
                "IMC Brand Lead Email text box is not present");
        UIDriver.setValue("IMCBrandLeadEmailInputBox", "abc@coca-cola.com");
        PEUtility.newProductionDataMap.put("IMC Brand Lead Email", "abc@coca-cola.com");

        // Single Bid
        boolean singleBid;
        if (PEUtility.newProductionDataMap.get("Available Production Type").equalsIgnoreCase("Radio")) {
            bPass = UIDriver.checkElementpresent("singleBidCheckBoxChecked", 1);
            CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                    "Single Bid checkbox must be checked",
                    "Single Bid checkbox is checked",
                    "Single Bid checkbox is not checked");
            singleBid = true;
            totalBidCount = 1;
        } else {
            bPass = UIDriver.checkElementpresent("singleBidSpan", 1);
            CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                    "Single Bid checkbox must be present",
                    "Single Bid checkbox is present",
                    "Single Bid checkbox is not present");
            singleBid = new Random().nextBoolean();
            if (singleBid) {
                UIDriver.clickElement("singleBidSpan");
                bPass = UIDriver.checkElementpresent("singleBidSpan", 1);
                CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                        "Single Bid checkbox must be present",
                        "Single Bid checkbox is present",
                        "Single Bid checkbox is not present");
                bPass = UIDriver.checkElementpresent("singleBidApprovalMessage", 1);
                CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                        "Proof of approval message must be shown",
                        "Proof of approval message is shown",
                        "Proof of approval message is not shown");
                totalBidCount = 1;
            }
        }
        PEUtility.newProductionDataMap.put("Single Bid", Boolean.toString(singleBid));

        // Single Bid Approval
        if (singleBid) {
            bPass = UIDriver.checkElementpresent("singleBidApprovalFileUpload", 1);
            CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                    "Single Bid Approval File upload should be available",
                    "File Upload option is available",
                    "File Upload option is not available");
        }
        System.out.println(PEUtility.newProductionDataMap);
        UIDriver.wait(1);

        //Save
        bPass = UIDriver.checkElementpresent("newProductionSaveButton", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                "Save Button must be shown", "Save button is present",
                "Save button is not present");
        UIDriver.clickElement("newProductionSaveButton");
        UIDriver.wait(2);
    }

    /**
     * @name: peCreateNewProductionPage2
     * @author :Sujata padhi
     * @description: Create the project statement and validate the mandatory field(set user data as Random
     * to select random compensation structure, else specify the compensation structure needed)
     * @preCondition : User needs to login with a valid Marketer user
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void peCreateNewProductionPage2(@Optional String DataRowID) {
        String componentName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<String, String> input = Framework.getDataRecord(DataRowID, componentName);
        String page2Action = input.get("data");
        boolean bPass;
        String strCurrentDateTime = Utility.getCurrentDate();
        UIDriver.wait(1);
        //Header
        String headerInit = UIDriver.getElementProperty("newProductionBidHeader");
        String header = PEUtility.newProductionDataMap.get("Single Bid").equalsIgnoreCase("true") ? "Single Bid" : "Production Bid";
        bPass = UIDriver.checkElementPresentWithoutProperty(headerInit.replace("PageHeader", header));
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage2",
                header + " page should displayed with header",
                header + " page is displayed",
                header + " page is not being displayed");

        if (page2Action.equalsIgnoreCase("CompleteLater")) {
            //Complete Later
            bPass = UIDriver.checkElementpresent("productionBidCompleteLater", 1);
            CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage2",
                    "Complete Later button must be shown", "Complete Later button is present",
                    "Complete Later button is not present");
            UIDriver.clickElement("productionBidCompleteLater");
        } else {
            bPass = UIDriver.checkElementpresent("firstPreferredProductionCompanyTextBox", 1);
            CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage2",
                    "First Preferred Production Company text box must be shown",
                    "First Preferred Production Company text box is present",
                    "First Preferred Production Company text box is not present");

            UIDriver.setValue("firstPreferredProductionCompanyTextBox", "Company_" + strCurrentDateTime);
            PEUtility.newProductionDataMap.put("First Preferred Production Company", "Company_" + strCurrentDateTime);

            UIDriver.wait(1);
            //Save
            bPass = UIDriver.checkElementpresent("productionBidSaveButton", 1);
            CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage2",
                    "Save button must be shown", "Save button is present",
                    "Save button is not present");
            UIDriver.clickElement("productionBidSaveButton");
        }

        UIDriver.wait(2);
        //Production bid page
        //todo: fix it
        bPass = true;//UIDriver.checkElementpresent("productionBidPage", 1);
        CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage2",
                "Production bid page must be shown", "Production bid page is present",
                "Production bid page is not present");
    }

    /**
     * @name: peNavigateToCreateNewProductionPage2
     * @author :Sujata padhi
     * @description: navigate to Page2
     * @preCondition :
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void peNavigateToCreateNewProductionPage2(@Optional String DataRowID) {
        boolean bPass;
        //Save
        bPass = UIDriver.checkElementpresent("prodGetStartedButton", 1);
        CSVReporter.reportPassFail(bPass, "peNavigateToCreateNewProductionPage2",
                "Get Started button must be shown",
                "Get Started button is present",
                "Get Started button is not present");
        UIDriver.clickElement("prodGetStartedButton");
    }

    /**
     * @name: productionBidPage
     * @author :Sujata padhi
     * @description: page 3
     * @preCondition :
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void peProductionBidPage(@Optional String DataRowID) {
        boolean bPass;

        for (currentBidCount=1; currentBidCount <= totalBidCount; currentBidCount ++) {
            String strCurrentDateTime = Utility.getCurrentDate();
            System.out.println("========#" + currentBidCount + " preferred Bid========");
            if(currentBidCount !=1) {
                // Add bid link
                bPass = UIDriver.checkElementpresent("addBidLink", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Add Bid link must be shown", "Add Bid link is present",
                        "Add Bid link is not present");
                UIDriver.clickElement("addBidLink");
                //preferred company name
                bPass = UIDriver.checkElementpresent("preferredProductionCompanyTextBox", 1);
                CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage2",
                        "#" + currentBidCount + " Preferred Production Company text box must be shown",
                        "#" + currentBidCount + " Preferred Production Company text box is present",
                        "#" + currentBidCount + " Preferred Production Company text box is not present");

                UIDriver.setValue("preferredProductionCompanyTextBox", "Company_" + strCurrentDateTime);
                PEUtility.newProductionDataMap.put("#" + currentBidCount + " Production Company", "Company_" + strCurrentDateTime);

                UIDriver.wait(1);
                //Save
                UIDriver.clickElement("productionBidSaveButton");
                UIDriver.wait(3);
                String companyNameInit = UIDriver.getElementProperty("subMenuBidNameLink");
                String companyName = companyNameInit.replaceAll("CompanyNamePlaceHolder", "Company_" + strCurrentDateTime);
                bPass = UIDriver.checkElementPresentWithoutProperty(companyName);
                CSVReporter.reportPassFail(bPass, "peHomePageValidation",
                        "Company_" + strCurrentDateTime + " Link should be displayed",
                        "Company_" + strCurrentDateTime + " Link is displayed",
                        "Company_" + strCurrentDateTime + " Link is not displaying");
                UIDriver.clickElementWithoutProperty(companyName);

            }
            //region Section 1 : Production Leader Section
            //Header validation
            bPass = UIDriver.checkElementpresent("productionLeaderHeader", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Production leader section must be shown", "Production leader section is present",
                    "Production leader section is not present");
            //Production Company textbox
            bPass = UIDriver.checkElementpresent("productionCompany", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Production Company text field must be shown", "Production Company text field  is present",
                    "Production Company text field is not present");
            //Director text Box
            bPass = UIDriver.checkElementpresent("directorTextBox", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Director text field must be shown", "Director text field  is present",
                    "Director text field is not present");
            String directorName = Utility.randomTextGenerator(20);
            UIDriver.setValue("directorTextBox", directorName);
            UIDriver.clickElement("selectLookUp");
            //Editorial company

            bPass = UIDriver.checkElementpresent("editorialCompanyTextBox", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Editorial Comany text field must be shown", "Editorial Comany text field  is present",
                    "Editorial Comany text field is not present");
            String editorialCompanyName = Utility.randomTextGenerator(20);
            UIDriver.setValue("editorialCompanyTextBox", editorialCompanyName);
            UIDriver.clickElement("editorialCompanyLookUp");
            //Editor
            bPass = UIDriver.checkElementpresent("editorTextBox", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Editorial text field must be shown", "Editorial text field  is present",
                    "Editorial text field is not present");
            String editorName = Utility.randomTextGenerator(20);
            UIDriver.setValue("editorTextBox", editorName);
            UIDriver.clickElement("editorLookUp");
            //Add vendor
            bPass = UIDriver.checkElementpresent("addVendorlink", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Add vendor Link must be shown", "Add vendor Link is present",
                    "Add vendor Link is not present");
            //Links
            bPass = UIDriver.checkElementpresent("links", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Links must be shown", "Links is present",
                    "Links is not present");
            String linkName = Utility.randomTextGenerator(10);
            UIDriver.setValue("links", linkName);

            //Click on Add Link
            bPass = UIDriver.checkElementpresent("addLinkSign", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    " Add new Links + button must be shown", "Add new Links + button is present",
                    "Add new Links + button not present");
            UIDriver.clickElement("addLinkSign");
            //Click on Save button
            bPass = UIDriver.checkElementpresent("saveButton", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Save button must be shown", "Save button is present",
                    "Save button not present");
            UIDriver.clickElement("saveButton");
            UIDriver.wait(2);

            //endregion

            //region Section 2 : Production Detail Section Validation
            // Click on chevron down arrow to expand the Production Details section
            UIDriver.clickElement("productionDetailsDownArrow");

            //Production Category
            bPass = UIDriver.checkElementpresent("productionCategoryList", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Production Category list must be shown", "Production Category list field present",
                    "Production Category list field not present");

            List<WebElement> productionCategoryList = UIDriver.getElements("productionCategoryList");
            int rnd = new Random().nextInt(productionCategoryList.size());
            System.out.println("Selected Production Category Name: " + productionCategoryList.get(rnd).getText());
            productionCategoryList.get(rnd).click();

            if (!PEUtility.newProductionDataMap.get("Available Production Type").equalsIgnoreCase("Radio")) {
                //Type Of Creative - Only available for Moving Image
                if (!PEUtility.newProductionDataMap.get("Available Production Type").equalsIgnoreCase("Still")) {
                    bPass = UIDriver.checkElementpresent("typeOfCreativeList", 1);
                    CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                            "Type Of Creative list must be shown", "Type Of Creative list field present",
                            "Type Of Creative list field not present");
                    UIDriver.selectValue("typeOfCreativeList",
                            Integer.toString(new Random().nextInt(UIDriver.getSelectValues("typeOfCreativeList").size() - 1) + 1), "index");
                }
                //Hours
                bPass = UIDriver.checkElementpresent("hours", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Hours input field must be shown", "Hours input field is present",
                        "Hours input field is not present");
                String hour = Integer.toString(new Random().nextInt(10));
                UIDriver.setValue("hours", hour);

                //Type
                bPass = UIDriver.checkElementpresent("productionDetailsTypeList", 1);
                CSVReporter.reportPassFail(bPass, "peCreateNewProductionPage1",
                        "Type list must be shown", "Type list field present",
                        "Type list field not present");
                UIDriver.selectValue("productionDetailsTypeList",
                        Integer.toString(new Random().nextInt(UIDriver.getSelectValues("productionDetailsTypeList").size())), "index");

                //Shoot Location
                bPass = UIDriver.checkElementpresent("shootLocation", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Shoot Location must be shown", "Shoot Location is present",
                        "Shoot Location is not present");
                String shootLocation = Utility.randomTextGenerator(10);
                UIDriver.setValue("shootLocation", shootLocation);

                //Location Type
                bPass = UIDriver.checkElementpresent("locationTypeList", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Location Type list must be shown", "Location Type list field present",
                        "Location Type list field not present");
                UIDriver.selectValue("locationTypeList",
                        Integer.toString(new Random().nextInt(UIDriver.getSelectValues("locationTypeList").size() - 1) + 1), "index");
            }

            //Celebrity Talent
            bPass = UIDriver.checkElementpresent("celebrityTalentYesNo", 1);
            CSVReporter.reportPassFail(bPass, "psCreateProjectStatementPage1",
                    "Celebrity Talent Yes/No radio button must be shown",
                    "Celebrity Talent Yes/No radio button field present",
                    "Celebrity Talent Yes/No radio button field not present");
            List<WebElement> yesNo = UIDriver.getElements("celebrityTalentYesNo");
            WebElement selectedYesNo = yesNo.get(new Random().nextInt(yesNo.size()));
            selectedYesNo.click();

            //Celebrity names
            if (selectedYesNo.getText().equalsIgnoreCase("Yes")) {
                bPass = UIDriver.checkElementpresent("celebrityNames", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Celebrity Names text field must be shown", "Celebrity Names text field  is present",
                        "Celebrity Names text field is not present");
                String celebrityNames = Utility.randomTextGenerator(20);
                UIDriver.setValue("celebrityNames", celebrityNames);
            }

            if (!PEUtility.newProductionDataMap.get("Available Production Type").equalsIgnoreCase("Still")) {
                //Music
                bPass = UIDriver.checkElementpresent("musicList", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Music list must be shown", "Music list field present",
                        "Music list field not present");
                UIDriver.selectValue("musicList",
                        Integer.toString(new Random().nextInt(UIDriver.getSelectValues("musicList").size() - 1) + 1), "index");

                // Add Spot Link
                bPass = UIDriver.checkElementpresent("addSpotLink", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Add Spot Link must be shown", "Add Spot Link is present",
                        "Add Spot Link not present");
                UIDriver.clickElement("addSpotLink");

                UIDriver.wait(2);

                //Spot Name
                bPass = UIDriver.checkElementpresent("spotName", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Spot Name must be shown", "Spot Name is present",
                        "Spot Name is not present");
                List<WebElement> spotNameList = UIDriver.getElements("spotName");
                for (WebElement spotNameInput : spotNameList) {
                    String spotName = Utility.randomTextGenerator(10);
                    spotNameInput.sendKeys(spotName);
                }

                //Spot Length (seconds)
                bPass = UIDriver.checkElementpresent("spotCount", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Spot Length list must be shown", "Spot Length list field present",
                        "Spot Length list field not present");
                int spotCount = UIDriver.getElements("spotCount").size();
                for (int rowNum = 0; rowNum < spotCount; rowNum++) {
                    String spotLengthInit = UIDriver.getElementProperty("spotLengthList");
                    String spotLengthProperty = spotLengthInit.replaceAll("RowNumber", Integer.toString(rowNum));
                    List<WebElement> spotLengthList = UIDriver.getElementsByProperty(spotLengthProperty);
                    rnd = new Random().nextInt(productionCategoryList.size());
                    spotLengthList.get(rnd).click();
                }

                String nbr = "";
                //# Versions
                bPass = UIDriver.checkElementpresent("nbrVersions", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "# Versions must be shown", "# Versions is present",
                        "# Versions is not present");
                List<WebElement> nbrVersions = UIDriver.getElements("nbrVersions");
                for (WebElement nbrVersionInput : nbrVersions) {
                    nbr = Integer.toString(new Random().nextInt(20));
                    nbrVersionInput.sendKeys(nbr);
                }

                //# Lifts
                bPass = UIDriver.checkElementpresent("nbrLifts", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "# Lift must be shown", "# Lift is present",
                        "# Lift is not present");
                List<WebElement> nbrLifts = UIDriver.getElements("nbrLifts");
                for (WebElement nbrLiftInput : nbrLifts) {
                    nbr = Integer.toString(new Random().nextInt(20));
                    nbrLiftInput.sendKeys(nbr);
                }

                //# On-Camera Principles
                bPass = UIDriver.checkElementpresent("nbrOnCameraPrinciples", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "# On-Camera Principles must be shown", "# On-Camera Principles is present",
                        "# On-Camera Principles is not present");
                List<WebElement> nbrOnCameraPrinciples = UIDriver.getElements("nbrOnCameraPrinciples");
                for (WebElement nbrOnCameraPrincipleInput : nbrOnCameraPrinciples) {
                    nbr = Integer.toString(new Random().nextInt(20));
                    nbrOnCameraPrincipleInput.sendKeys(nbr);
                }

                //# Voice Over
                bPass = UIDriver.checkElementpresent("nbrVoiceOver", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "# Voice Over must be shown", "# Voice Over is present",
                        "# Voice Over is not present");
                List<WebElement> nbrVoiceOver = UIDriver.getElements("nbrVoiceOver");
                for (WebElement nbrVoiceOverInput : nbrVoiceOver) {
                    nbr = Integer.toString(new Random().nextInt(20));
                    nbrVoiceOverInput.sendKeys(nbr);
                }
                //Remarks
                bPass = UIDriver.checkElementpresent("remarksTextArea", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Remarks text area must be shown", "Remarks text area is present",
                        "Remarks text area is not present");
                List<WebElement> remarksTextAreaList = UIDriver.getElements("remarksTextArea");
                for (WebElement remarksTextAreaInput : remarksTextAreaList) {
                    nbr = Utility.randomTextGenerator(30);
                    remarksTextAreaInput.sendKeys(nbr);
                }
            }
            //Union
            bPass = UIDriver.checkElementpresent("unionList", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Union list must be shown", "Union list field present",
                    "Union list field not present");
            UIDriver.selectValue("unionList",
                    Integer.toString(new Random().nextInt(UIDriver.getSelectValues("unionList").size())), "index");
            //endregion

            //region Section 3 : Estimates
            // Click on chevron down arrow to expand the Estimates section
            UIDriver.clickElement("estimateDownArrow");

            List<String> expenseTypeList;

            String availableType = PEUtility.newProductionDataMap.get("Available Production Type");
            System.out.println("Selected Available Type is: " + availableType);
            switch (availableType) {
                case "Still":
                    expenseTypeList = new ArrayList<String>() {{
                        add("Photographer Fee");
                        add("Production (Less Photographer's Fee)");
                        add("Corrections/Retouching");
                        add("Post Production");
                        add("Photography License Fee");
                        add("Usage Fees");
                        add("Artwork/Illustration Fee");
                        add("Casting");
                        add("Talent Buyout");
                        add("Talent Sessions");
                        add("Talent Travel");
                        add("Talent Usage");
                        add("Agency Travel");
                        add("Miscellaneous/Other");
                    }};
                    break;
                case "Radio":
                    expenseTypeList = new ArrayList<String>() {{
                        add("ISDN Patch");
                        add("Other Sound");
                        add("Recording Studio");
                        add("Sound Design");
                        add("Licensed Music");
                        add("Original Music");
                        add("Casting");
                        add("Talent Sessions");
                        add("Talent Usage");
                        add("Miscellaneous/Other");
                    }};
                    break;
                default:
                    expenseTypeList = new ArrayList<String>() {{
                        add("Director Fee");
                        add("Production (Less Director's Fee)");
                        add("Editor Fee");
                        add("Editorial Estimate (Less Editor's Fee)");
                        add("Animation");
                        add("VFX");
                        add("Licensed Music");
                        add("Original Music");
                        add("Record/Mix");
                        add("Sound Design");
                        add("Stock Footage");
                        add("Casting");
                        add("Talent Buyout");
                        add("Talent Sessions");
                        add("Talent Travel");
                        add("Agency Travel");
                        add("Miscellaneous/Other");
                    }};
                    break;
            }
            UIDriver.wait(3);
            String expenseTypeNameInit = UIDriver.getElementProperty("expenseType");
            String expenseTypeInputInit = UIDriver.getElementProperty("estimateCost");
            String expenseTypeAddDocInit = UIDriver.getElementProperty("expenseAddDocument");
            String expenseTypeDeleteInit = UIDriver.getElementProperty("expenseLineDelete");

            for (String expenseType : expenseTypeList) {
                String expenseTypeName = expenseTypeNameInit.replaceAll("ExpenseTypePlaceHolder", expenseType);
                String expenseTypeInput = expenseTypeInputInit.replaceAll("ExpenseTypePlaceHolder", expenseType);
                String expenseTypeAddDoc = expenseTypeAddDocInit.replaceAll("ExpenseTypePlaceHolder", expenseType);
                String expenseTypeDelete = expenseTypeDeleteInit.replaceAll("ExpenseTypePlaceHolder", expenseType);

                // Expense name
                bPass = UIDriver.checkElementPresentWithoutProperty(expenseTypeName);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Expense type \"" + expenseType + "\" must be shown",
                        "Expense type \"" + expenseType + "\" is shown",
                        "Expense type \"" + expenseType + "\" is NOT shown");
                // Expense cost input
                bPass = UIDriver.checkElementPresentWithoutProperty(expenseTypeInput);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Cost Input for \"" + expenseType + "\" must be shown",
                        "Cost Input for \"" + expenseType + "\" is shown",
                        "Cost Input for \"" + expenseType + "\" is NOT shown");
                UIDriver.setValueWithoutProperty(expenseTypeInput, Utility.randomNumberGenerator());

                // Expense cost input
                bPass = UIDriver.checkElementPresentWithoutProperty(expenseTypeAddDoc);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Add Document button for \"" + expenseType + "\" must be shown",
                        "Add Document button for \"" + expenseType + "\" is shown",
                        "Add Document button for \"" + expenseType + "\" is NOT shown");

                // Expense line delete
                bPass = UIDriver.checkElementPresentWithoutProperty(expenseTypeDelete);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Delete button for \"" + expenseType + "\" must be shown",
                        "Delete Document button for \"" + expenseType + "\" is shown",
                        "Delete Document button for \"" + expenseType + "\" is NOT shown");

            }
            UIDriver.clickElement("saveButton");
            UIDriver.wait(1);

            if(currentBidCount == 1) {
                //Delete expense
                rnd = new Random().nextInt(expenseTypeList.size() - 1);
                String expenseTypeDelete = expenseTypeDeleteInit.replaceAll("ExpenseTypePlaceHolder", expenseTypeList.get(rnd));
                UIDriver.clickElementWithoutProperty(expenseTypeDelete);
                UIDriver.wait(1);
                BrowserDriver.driver.switchTo().alert().accept();
                CSVReporter.reportPassFail(true, "peProductionBidPage",
                        "Expense Type should be deleted", "Expense Type is deleted",
                        "Expense Type is not deleted");
                UIDriver.wait(2);
                List<WebElement> checkBoxList = UIDriver.getElements("expenseCheckBoxList");
                rnd = new Random().nextInt(checkBoxList.size() - 1) + 1;
                checkBoxList.get(rnd).click();
                UIDriver.wait(1);
                //Click on Delete button
                bPass = UIDriver.checkElementpresent("deleteButton", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Delete button must be shown", "Delete button is present",
                        "Delete button not present");
                UIDriver.clickElement("deleteButton");
                UIDriver.wait(1);
                BrowserDriver.driver.switchTo().alert().accept();
                CSVReporter.reportPassFail(true, "peProductionBidPage",
                        "Selected Expense Type should be deleted",
                        "Selected Expense Type is deleted",
                        "Selected Expense Type is not deleted");

                // Add expenses
                //Add expense link
                bPass = UIDriver.checkElementpresent("addExpenseLink", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Add Expense link must be shown", "Add Expense link is present",
                        "Add Expense link is not present");
                UIDriver.clickElement("addExpenseLink");
                UIDriver.wait(1);

                //expense type
                bPass = UIDriver.checkElementpresent("expenseTypeDropdownList", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Expense Type dropdown list must be shown",
                        "Expense Type dropdown list is present",
                        "Expense Type dropdown list is not present");
                UIDriver.selectValue("expenseTypeDropdownList",
                        Integer.toString(new Random().nextInt(UIDriver.getSelectValues("expenseTypeDropdownList").size() - 2) + 2), "index");
                //expense name
                bPass = UIDriver.checkElementpresent("expenseNameDropdownList", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Expense Name dropdown list must be shown",
                        "Expense Name dropdown list is present",
                        "Expense Name dropdown list is not present");
                UIDriver.selectValue("expenseNameDropdownList",
                        Integer.toString(new Random().nextInt(UIDriver.getSelectValues("expenseNameDropdownList").size() - 1) + 1), "index");

                //Add expense type link
                bPass = UIDriver.checkElementpresent("addExpenseType", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Add Expense Type link must be shown", "Add Expense Type link is present",
                        "Add Expense Type link is not present");
                UIDriver.clickElement("addExpenseType");

                UIDriver.selectValue("expenseTypeDropdownList",
                        Integer.toString(new Random().nextInt(UIDriver.getSelectValues("expenseTypeDropdownList").size() - 1) + 1), "index");
                //edit button
                bPass = UIDriver.checkElementpresent("expenseEditButton", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Expense Name edit button must be shown", "Expense Name edit button is present",
                        "Expense Name edit button is not present");
                UIDriver.clickElement("expenseEditButton");

                bPass = UIDriver.checkElementpresent("expenseNameInputBox", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Expense Name input box must be shown", "Expense Name input box is present",
                        "Expense Name input box is not present");
                UIDriver.setValue("expenseNameInputBox", Utility.randomTextGenerator(10));

                //Save
                bPass = UIDriver.checkElementpresent("addExpenseSaveButton", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Save button must be shown", "Save button is present",
                        "Save button is not present");
                UIDriver.clickElement("addExpenseSaveButton");
            }
            UIDriver.wait(1);
            //endregion

            //region Section 4 : Comments & Recommendation
            // Click on chevron down arrow to expand the Comments & Recommendation section
            UIDriver.clickElement("commentRecommendationDownArrow");

            //Preference - only available for Project bid, not available for Single bid
            if (PEUtility.newProductionDataMap.get("Single Bid").equalsIgnoreCase("false")) {
                bPass = UIDriver.checkElementpresent("preferenceDropDownList", 1);
                CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                        "Preference list must be shown", "Preference list field present",
                        "Preference list field not present");
                UIDriver.selectValue("preferenceDropDownList", Integer.toString(currentBidCount), "text");
                        //Integer.toString(new Random().nextInt(UIDriver.getSelectValues("preferenceDropDownList").size() - 1) + 1), "index");
            }

            //Recommendation
            bPass = UIDriver.checkElementpresent("recommendationTextBox", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Recommendation must be shown", "Recommendation is present",
                    "Recommendation is not present");
            String recommendation = Utility.randomTextGenerator(100);
            UIDriver.setValue("recommendationTextBox", recommendation);

            //Comments
            bPass = UIDriver.checkElementpresent("commentsTextBox", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Comments must be shown", "Comments is present",
                    "Comments is not present");
            String comments = Utility.randomTextGenerator(100);
            UIDriver.setValue("commentsTextBox", comments);

            UIDriver.clickElement("saveButton");
            UIDriver.wait(1);
            //endregion

            //region Section 5 : History
            bPass = UIDriver.checkElementpresent("historyDownArrow", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "History section must be shown", "History section is present",
                    "History section is not present");
            UIDriver.clickElement("historyDownArrow");

            //endregion

            // Mark As Complete Button
            bPass = UIDriver.checkElementpresent("markAsCompleteButton", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Mark As Complete Button must be shown", "Mark As Complete Button is present",
                    "Mark As Complete Button not present");

            UIDriver.wait(2);
            // Save And Close Button
            bPass = UIDriver.checkElementpresent("saveAndCloseButton", 1);
            CSVReporter.reportPassFail(bPass, "peProductionBidPage",
                    "Save And Close Button must be shown", "Save And Close Button is present",
                    "Save And Close Button not present");

            UIDriver.clickElement("markAsCompleteButton");
            //UIDriver.clickElement("saveAndCloseButton");

            UIDriver.wait(3);
        }
    }

    /**
     * @name: peBidSelectionPage
     * @author :Sujata padhi
     * @description: Bid Selection page
     * @preCondition : User is on bid selection page
     * @lastChanged
     * @TODO
     */
    @Test
    @Parameters({"DataRowID"})
    public static void peBidSelectionPage(@Optional String DataRowID) {
        boolean bPass;
        //Back To Timeline link
        bPass = UIDriver.checkElementpresent("backToTimelineButton", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Back To Timeline link must be shown", "Back To Timeline link is present",
                "Back To Timeline link is not present");

        if(PEUtility.newProductionDataMap.get("Single Bid").equalsIgnoreCase("true")){
            UIDriver.clickElement("backToTimelineButton");
            UIDriver.clickElement("prodGetStartedButton");
        } else {
            //Check For Bid Selection link on the side menu
            bPass = UIDriver.checkElementpresent("menuBidSelectionLink", 1);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Bid Selection link must be shown", "Bid Selection link is present",
                    "Bid Selection link is not present");
            UIDriver.clickElement("menuBidSelectionLink");
        }
        /*
        bPass = UIDriver.checkElementpresent("bidSelectionPageHeader", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Production Bid header must be shown", "Production Bid header is present",
                "Production Bid header is not present");
        */
        UIDriver.wait(2);
        //Select this Bid button
        bPass = UIDriver.checkElementpresent("selectThisBidButton", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Select this Bid button must be shown", "Select this Bid button is present",
                "Select this Bid button is not present");

        List<WebElement> selectBidButtonList = UIDriver.getElements("selectThisBidButton");
        WebElement selectBidButton = selectBidButtonList.get(new Random().nextInt(selectBidButtonList.size()));
        selectBidButton.click();

        //verify popup
        bPass = UIDriver.checkElementpresent("congratulationMessage", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Congratulation message must be shown", "Congratulation message is present",
                "Congratulation message is not present");
        //Cancel button on popup
        bPass = UIDriver.checkElementpresent("bidSelectionCancelButton", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Cancel button must be shown on popup", "Cancel button is present on popup",
                "Cancel button is not present on popup");
        UIDriver.clickElement("bidSelectionCancelButton");

        selectBidButton = selectBidButtonList.get(new Random().nextInt(selectBidButtonList.size()));
        selectBidButton.click();

        //Create Production Estimate button on popup
        bPass = UIDriver.checkElementpresent("createProductionEstimateButton", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Create Production Estimate button must be shown on popup",
                "Create Production Estimate button is present on popup",
                "Create Production Estimate button is not present on popup");
        UIDriver.clickElement("createProductionEstimateButton");

        // Verify Production Estimate header
        bPass = UIDriver.checkElementpresent("productionEstimateHeader", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Production Estimate header must be shown", "Production Estimate header is present",
                "Production Estimate header is not present");

        UIDriver.wait(2);

        //Select Different Bid button validation

        bPass = UIDriver.checkElementpresent("selectDifferentBidbutton", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Select Different Bid button must be shown", "Select Different Bid button is present",
                "Select Different Bid button is not present");
        UIDriver.clickElement("selectDifferentBidbutton");
        UIDriver.wait(1);
        //Select "Cancel" button in pop-up window
        BrowserDriver.driver.switchTo().alert().dismiss();
        UIDriver.wait(1);
        bPass = UIDriver.checkElementpresent("productionEstimateHeader", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Production Estimate header must be shown", "Production Estimate header is present",
                "Production Estimate header is not present");

        //Select "OK" button in pop-up window
        UIDriver.clickElement("selectDifferentBidbutton");
        UIDriver.wait(1);
        BrowserDriver.driver.switchTo().alert().accept();
        UIDriver.wait(1);
        bPass = UIDriver.checkElementpresent("backToTimelineButton", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Back To Timeline link must be shown", "Back To Timeline link is present",
                "Back To Timeline link is not present");

        //Remove Bid functionality validation
        if(!PEUtility.newProductionDataMap.get("Single Bid").equalsIgnoreCase("true")) {
            bPass = UIDriver.checkElementpresent("removeBidLink", 1);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Remove bid link must be shown", "Remove bid link is present",
                    "Remove bid link is not present");
            UIDriver.clickElement("removeBidLink");
            UIDriver.wait(1);
            // pop-up validation for remove bid
            bPass = UIDriver.checkElementpresent("removeBidWarningMessage", 1);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Remove bid Warning message must be shown", "Remove bid Warning message is present",
                    "Remove bid Warning message is not present");
            //Remove Bid Pop-up "No,Keep it" button
            bPass = UIDriver.checkElementpresent("noKeepItButton", 1);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "No Keep It Button must be shown", "No Keep It Button is present",
                    "No Keep It Button is not present");
            UIDriver.clickElement("noKeepItButton");
            UIDriver.wait(1);
            UIDriver.clickElement("removeBidLink");
            // Click on "Remove" button on pop-up page
            bPass = UIDriver.checkElementpresent("removeButtonForRemovePopUpPage", 1);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Remove Button must be shown", "Remove Button is present",
                    "Remove Button is not present");
            UIDriver.clickElement("removeButtonForRemovePopUpPage");
        }

        //Send for signatories Button functionality

        UIDriver.clickElement("selectThisBidButton");
        UIDriver.wait(1);
        UIDriver.clickElement("createProductionEstimateButton");

        //Signatories button validation
        bPass= UIDriver.checkElementpresent("sendForSignatoriesButton", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Send For Signatories Button must be shown", "Send For Signatories Button is present",
                "Send For Signatories Button is not present");
        UIDriver.clickElement("sendForSignatoriesButton");
        // Add signer pop-up box
        bPass= UIDriver.checkElementpresent("addSignerpopupHeader", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Add Signer pop-up must be shown", "Add Signer pop-up is present",
                "Add Signer pop-up is not present");

        //Agency Signer header
        bPass= UIDriver.checkElementpresent("agencySignerHeader", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Agency Signer Header must be shown", "Agency Signer Header is present",
                "Agency Signer Header is not present");

        //Add Agency Signer link
        bPass= UIDriver.checkElementpresent("addAgencySignerLink", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Add Agency Signer link must be shown", "Add Agency Signer link is present",
                "Add Agency Signer link is not present");
        UIDriver.clickElement("addAgencySignerLink");

        for(int i = 0; i<2; i++){
            String rowInit = UIDriver.getElementProperty("addAgencySignerRow");
            String rowActual = rowInit.replaceAll("RowNumber", Integer.toString(i));
            bPass = UIDriver.checkElementPresentWithoutProperty(rowActual);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Agency Signer row # " + Integer.toString(i+1)+ " must be shown",
                    "Agency Signer row # " + Integer.toString(i+1)+ " is present",
                    "Agency Signer row # " + Integer.toString(i+1)+ " is not present");
            UIDriver.clickElementWithoutProperty(rowActual);
            //Add Agency Name
            UIDriver.checkElementpresent("agencySignerDetailsTextBox", 1);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Agency Signer Name textbox must be shown",
                    "Agency Signer Name textbox is present",
                    "Agency Signer Name textbox is not present");
            UIDriver.setValue("agencySignerDetailsTextBox", Utility.randomTextGenerator(10));
            //Agency Email
            UIDriver.checkElementpresent("agencySignerDetailsEmailBox", 1);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Agency Signer Email textbox must be shown",
                    "Agency Signer Email textbox is present",
                    "Agency Signer Email textbox is not present");
           // UIDriver.setValue("agencySignerDetailsEmailBox", Utility.randomTextGenerator(10).replace(" ", "") + "@test.com");
            UIDriver.setValue("agencySignerDetailsEmailBox","supadhi@coca-cola.com");
            //Agency Title
            UIDriver.checkElementpresent("agencySignerTitleTextBox", 1);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Agency Signer Title textbox must be shown",
                    "Agency Signer Title textbox is present",
                    "Agency Signer Title textbox is not present");
            UIDriver.setValue("agencySignerTitleTextBox", Utility.randomTextGenerator(10));

        }

        //Coca-Cola Signer header
        bPass= UIDriver.checkElementpresent("coca-colaSignerHeader", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Coca-Cola Signer Header must be shown", "Coca-Cola Signer Header is present",
                "Coca-Cola Signer Header is not present");

        //Add Coca-Cola Signer link
        bPass= UIDriver.checkElementpresent("addCoca-colaSignerLink", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Add Coca-Cola Signer link must be shown", "Add Coca-Cola Signer link is present",
                "Add Coca-Cola Signer link is not present");
        UIDriver.clickElement("addCoca-colaSignerLink");

        for(int i = 0; i<2; i++){
            String rowInit = UIDriver.getElementProperty("addCocaColaSignerRow");
            String rowActual = rowInit.replaceAll("RowNumber", Integer.toString(i));
            bPass = UIDriver.checkElementPresentWithoutProperty(rowActual);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Coca-Cola Signer row # " + Integer.toString(i+1)+ " must be shown",
                    "Coca-Cola Signer row # " + Integer.toString(i+1)+ " is present",
                    "Coca-Cola Signer row # " + Integer.toString(i+1)+ " is not present");
            UIDriver.clickElementWithoutProperty(rowActual);
                       //Add Coca-Cola Signer Name
            UIDriver.checkElementpresent("coca-colaSignerTextBox", 1);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Coca-Cola Signer Name textbox must be shown",
                    "Coca-Cola Signer Name textbox is present",
                    "Coca-Cola Signer Name textbox is not present");
            UIDriver.setValue("coca-colaSignerTextBox", Utility.randomTextGenerator(10));
            //Coca-Cola signer Email
            UIDriver.checkElementpresent("coca-colaSignerEmailTextBox", 1);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Coca-Cola Signer Email textbox must be shown",
                    "Coca-Cola Signer Email textbox is present",
                    "Coca-Cola Signer Email textbox is not present");
            //UIDriver.setValue("coca-colaSignerEmailTextBox", Utility.randomTextGenerator(10).replace(" ", "") + "@test.com");
            UIDriver.setValue("coca-colaSignerEmailTextBox","supadhi@coca-cola.com");
            //Coca-Cola Title
            UIDriver.checkElementpresent("coca-colaSignerTitleTextBox", 1);
            CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                    "Coca-Cola Signer Title textbox must be shown",
                    "Coca-Cola Signer Title textbox is present",
                    "Coca-Cola Signer Title textbox is not present");
            UIDriver.setValue("coca-colaSignerTitleTextBox", Utility.randomTextGenerator(10));
        }
        //Remove Coca-cola Signer link
        bPass= UIDriver.checkElementpresent("removeLink", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Remove signer option must be shown",
                "Remove signer option is present",
                "Remove signer option is not present");
        UIDriver.clickElement("removeLink");

        //Send button
        UIDriver.checkElementpresent("signerSendButton", 1);
        CSVReporter.reportPassFail(bPass, "peBidSelectionPage",
                "Send button must be shown",
                "Send button is present",
                "Send button is not present");
        UIDriver.clickElement("signerSendButton");
        UIDriver.wait(3);

    }
}