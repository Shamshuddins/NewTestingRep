package main.java.module;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import javax.script.ScriptException;

import com.relevantcodes.extentreports.ExtentTest;
import main.java.framework.*;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestNG;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import org.testng.xml.XmlInclude;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;





public class BaseTest extends BrowserDriver {
	public static int intM;
	public static int intC=0;
	static TestNG runner ;
	HashMap<String, String> output = new HashMap<>();
	public static String DataRowID;
	public static String componentName;
	public static String userType="";
	public static ITestContext context;
	public static String[] aDataRowID;
	public static String previousComponent="";
	public static String previousTest="";
	public static String className;
	public static String strEnv;
	public static FileInputStream fis;
	public static Properties prop;


	public static void main(String[] args) throws IOException, ScriptException, InstantiationException, IllegalAccessException, IllegalThreadStateException {

		List<String> lstBrowsers = new LinkedList<String>();
		lstBrowsers.add("chrome");
		lstBrowsers.add("ie");
		lstBrowsers.add("firefox");

		File src = new File("./environment.property");
		fis = new FileInputStream(src);
		prop = new Properties();
		prop.load(fis);

		String strBrowser = args[0];
		if(!lstBrowsers.contains(strBrowser.toLowerCase())){
			System.out.println("Please provide any of the "+ lstBrowsers.toString().replace("]","").replace("[","") +" browser");
			return;
		}

		for(String appName : prop.getProperty("AppList").split("\\|")) {
			new TestDriver(args[1] + File.separator + appName + File.separator + "Driver");
			strEnv = args[2];
			//new TestReporter();
			new CSVReporter();
			//new Reporter();
			TestDriver.extent = new ExtentReports(TestDriver.reportFolder + File.separator + "ExecutionReport.html", true);
			BrowserDriver.strBrowser = strBrowser;
			BrowserDriver.getDriver(strBrowser);

			XMLDriver.createTestNGXML(appName);
			// Create a list of String
			List<String> suitefiles = new ArrayList<String>();
			suitefiles.clear();
			String sTestNGPath = "testng.xml";

			// Add xml file which you have to execute
			suitefiles.add(sTestNGPath);

			// now set xml file for execution
			runner = new TestNG();
			runner.setTestSuites(suitefiles);
			runner.run();
		}
	}


	@BeforeSuite
	public void beforeSuite(){
		TestDriver.extent = new ExtentReports (TestDriver.reportFolder + File.separator + "ExecutionReport.html",true);

	}


	@BeforeTest
	public void getTestName(ITestContext testContext) {
		TestDriver.testName = testContext.getName();
		TestDriver.logger = TestDriver.extent.startTest(TestDriver.testName);
		intM=0;
		Framework.result="Pass";
		System.out.println("=======Test Execution Starts:"+TestDriver.testName);
	}


	@AfterSuite
	public void quitBrowser() {
		//Close browser
		if (null != BrowserDriver.driver) {
			BrowserDriver.driver.quit();
			BrowserDriver.driver = null;
		}
	}


	@AfterSuite
	public void clearCachedData() {
		TestDriver.extent.flush();
		TestDriver.extent.close();
		UIDriver.OR.clear();
	}

	@AfterTest
	public void writeSummary(){
		String[] arrSummaryRecord={TestDriver.testName,Framework.result};
		try {
			CSV_IO.writeinCSV(CSVReporter.csvSummaryReport, arrSummaryRecord);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TestDriver.extent.endTest(TestDriver.logger);
	}

}