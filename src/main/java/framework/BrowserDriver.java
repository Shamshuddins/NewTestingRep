package main.java.framework;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class BrowserDriver {

	public static WebDriver driver;
	public static String sDriver;
	public static String strBrowser;
	public static String chromeDriverPlatformVersion;
	public static String strBrowserDriver;

	public static WebDriver getDriver(String strDriver) {

		WebDriver objDriver = null;

		//String sUserName = System.getProperty("user.name");
		if (strDriver.equalsIgnoreCase("chrome")) {
			String OS = System.getProperty("os.name");
			System.out.println("------" + OS + "------");
			if (OS.startsWith("Linux")) {
				chromeDriverPlatformVersion = "chromedriver";
				strBrowserDriver = TestDriver.folderpath + File.separator + "BrowserDrivers" + File.separator + chromeDriverPlatformVersion;
			}
			else if (OS.startsWith("Windows")) {
				chromeDriverPlatformVersion = "chromedriver.exe";
				strBrowserDriver = TestDriver.folderpath + "/BrowserDrivers/" + chromeDriverPlatformVersion;
			}
			//String strBrowserDriver = TestDriver.folderpath + System.getProperty("chromeDriverName");
			File driverFile = new File(strBrowserDriver);
			driverFile.setExecutable(true);
			System.setProperty("webdriver.chrome.driver", strBrowserDriver);

			ChromeOptions options = new ChromeOptions();
			options.addArguments("chrome.switches", "--disable-extensions");
			options.addArguments("--start-maximized");
			//options.addArguments("--headless");

			/* Adding Chrome profile by .addArguments to objChrome_Profile */
			// options.addArguments("user-data-dir=" + Chrome_Profile_Path);

			/*
			 * Initializing the Webdriver instance (i.e. driver) to open Chrome Browser and
			 * passing the Chrome Profile as argument
			 */
			objDriver = new ChromeDriver(options);

			// DesiredCapabilities capability = DesiredCapabilities.chrome();

			// System.setProperty("webdriver.chrome.driver", "path to chromedriver.exe");
			// capability.setBrowserName("chrome");
			// capability.setPlatform(PlatformAndEnvironmentSetUp.platformSetUp);

		} else if (strDriver.equalsIgnoreCase("IE")) {
			strBrowserDriver = TestDriver.folderpath + "//BrowserDrivers//IEDriverServer.exe";
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			File file = new File(strBrowserDriver);
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			objDriver = new InternetExplorerDriver(capabilities);
			// objDriver=new InternetExplorerDriver();
		} else if (strDriver.equalsIgnoreCase("firefox")) {
			strBrowserDriver = TestDriver.folderpath + "//BrowserDrivers//geckodriver.exe";
			System.setProperty("webdriver.gecko.driver",strBrowserDriver);
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability("marionette",true);
			objDriver = new FirefoxDriver(capabilities);
		}
		driver = objDriver;
		return objDriver;
	}
}