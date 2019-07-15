package main.java.listener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import main.java.framework.BrowserDriver;
/*import main.java.framework.DriverBase;*/
import main.java.framework.TestDriver;
import main.java.module.BaseTest;


public class Listener extends BaseTest implements ITestListener {


    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("*** " +  getTestMethodName(iTestResult) + " started");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println(getTestMethodName(iTestResult) + " succeed");
        
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        String methodName = iTestResult.getName();
        if(iTestResult.isSuccess()){
            File scrFile = ((TakesScreenshot)BrowserDriver.driver).getScreenshotAs(OutputType.FILE);
            try {
                String reportDirectory =  TestDriver.screenshotFolder;
                File destFile = new File((String) reportDirectory+"/success_screenshots/"+methodName+"_"+formater.format(calendar.getTime())+".png");
                FileUtils.copyFile(scrFile, destFile);
                //Reporter.log("<a href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println(getTestMethodName(iTestResult) + " failed");
        
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        String methodName = iTestResult.getName();
        if(!iTestResult.isSuccess()){
            File scrFile = ((TakesScreenshot)BrowserDriver.driver).getScreenshotAs(OutputType.FILE);
            try {
                String reportDirectory =  TestDriver.screenshotFolder;
                File destFile = new File((String) reportDirectory+"/failure_screenshots/"+methodName+"_"+formater.format(calendar.getTime())+".png");
                FileUtils.copyFile(scrFile, destFile);
                Reporter.log("<a href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("### "+  getTestMethodName(iTestResult) + " skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("### " + iTestContext.getName() + " started");
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println(iTestContext.getName()+ " completed");
    }

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }
    
}