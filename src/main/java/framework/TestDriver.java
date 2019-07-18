package main.java.framework;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class TestDriver {
	public static int intRowNum;
	public static int intColNum;
	public static int intColCount;
	public static int intRowCount;
	public static int[] iCellPos;
	public static boolean boolFLag;
	public static String strFilePath;
	public static String strSheetName;
	public static String strDataSheetName;
	public static String strDataRowID;
	public static String reportPath;
	public static String reportTemplate;
	public static String testSetPath;
	public static String folderpath;
	public static int reporterRow = 2;
	public static String reportFolder;
	public static String testNGReportFolder;
	public static String testName;
	public static String screenshotFolder;
	public static String executionSummary;
	public static HashMap<String, String> input;
	public static ExtentTest logger;
	public static ExtentReports extent;


	// Method for Initializing All Variables
	public TestDriver(String strFolderPath) throws IOException, ScriptException {

		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
		String strDataSheetPath = strFolderPath + "/Data/TestData.csv";
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String strTimeStamp = timeStamp.replace(".", "_");
		String strReportFolder = strFolderPath + File.separator + "Logs" + File.separator + "ExecutionReport_" + strTimeStamp;
		File objFile = new File(strReportFolder);
		objFile.mkdir();
		String strReportPath = strReportFolder + File.separator + "ExecutionReport.csv";
		String strSummaryReport=strReportFolder + File.separator + "ExecutionSummary.csv";

        String strScreenShotFolder=strReportFolder + File.separator +"ScreenShots";

		File objFile1 = new File(strScreenShotFolder);
		objFile1.mkdirs();
		String strReportTemplate = strFolderPath + File.separator + "Report Template"+ File.separator +"ReportTemplate.csv";
		String strTestSetPath = strFolderPath + File.separator +"TestSet.csv";

		TestDriver.strDataSheetName = strDataSheetPath;
		TestDriver.reportPath = strReportPath;
		TestDriver.reportTemplate = strReportTemplate;
		TestDriver.folderpath = strFolderPath;
		TestDriver.testSetPath = strTestSetPath;
		TestDriver.reportFolder = strReportFolder;
		TestDriver.executionSummary = strSummaryReport;
		TestDriver.screenshotFolder = strScreenShotFolder;
		// TestDriver.createWorkBookObject();
		// TestDriver.getSheetObject();

		UIDriver.readOR();

	}

}
