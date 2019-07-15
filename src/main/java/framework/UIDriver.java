package main.java.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UIDriver {

	public static List<HashMap<String, String>> OR;

	public static void readOR() throws IOException, ScriptException {
		String strORFilePath = TestDriver.folderpath + "/OR/Repository.csv";
		List<HashMap<String, String>> objOR = CSV_IO.readCSVFile(strORFilePath);
		UIDriver.OR = objOR;

	}

	public static boolean checkElementpresentDOM(String strElementName, int iTimeOut) {
		boolean bElementPresent = false;
		int intCount = 0;
		while (intCount < iTimeOut) {
			try {
				bElementPresent = (UIDriver.getElements(strElementName).size() > 0);
			} catch (Exception e) {
				bElementPresent = false;
			}
			intCount++;
			if (bElementPresent == false) {
				UIDriver.wait(1);
			} else {
				break;
			}
		}
		if (bElementPresent == false) {
			System.out.println("Element Not Found:" + strElementName);
		}

		return bElementPresent;

	}

	public static boolean checkElementpresentDOM(String strElementName, String sParam, int iTimeOut) {
		boolean bElementPresent = false;
		int intCount = 0;
		while (intCount < iTimeOut) {
			try {
				bElementPresent = (UIDriver.getElements(strElementName, sParam).size() > 0);
			} catch (Exception e) {
				bElementPresent = false;
			}
			intCount++;
			if (bElementPresent == false) {
				UIDriver.wait(1);
			} else {
				break;
			}
		}
		if (bElementPresent == false) {
			System.out.println("Element Not Found:" + strElementName);
		}

		return bElementPresent;

	}

	public static boolean checkElementpresent(String strElementName, int iTimeOut) {
		boolean bElementPresent = false;
		int intCount = 0;
		while (intCount < iTimeOut) {
			try {
				bElementPresent = UIDriver.getElement(strElementName).isDisplayed();
			} catch (Exception e) {
				bElementPresent = false;
			}
			intCount++;
			if (bElementPresent == false) {
				UIDriver.wait(3);
			} else {
				break;
			}
		}
		if (bElementPresent == false) {
			System.out
					.println("Element Not Found:" + strElementName + ":" + UIDriver.getElementProperty(strElementName));
		}

		return bElementPresent;

	}

	public static boolean checkElementpresentDynamic(String strElementName, String sParamValue, int iTimeOut) {
		boolean bElementPresent = false;
		int intCount = 0;
		while (intCount < iTimeOut) {
			try {
				bElementPresent = UIDriver.getElementDynamic(strElementName, sParamValue).isDisplayed();
			} catch (Exception e) {
				bElementPresent = false;
			}
			intCount++;
			if (bElementPresent == false) {
				UIDriver.wait(1);
			} else {
				break;
			}
		}
		if (bElementPresent == false) {
			System.out.println("Element Not Found:" + strElementName + ":"
					+ UIDriver.getElementProperty(strElementName).replace("#param#", sParamValue));
		}
		return bElementPresent;

	}

	public static boolean checkElementpresentDynamic(String strElementName, String sParam1, String sParam2,
													 int iTimeOut) {
		boolean bElementPresent = false;
		int intCount = 0;
		while (intCount < iTimeOut) {
			try {
				bElementPresent = UIDriver.getElementDynamic(strElementName, sParam1, sParam2).isDisplayed();
			} catch (Exception e) {
				bElementPresent = false;
			}
			intCount++;
			if (bElementPresent == false) {
				UIDriver.wait(1);
			} else {
				break;
			}
		}
		if (bElementPresent == false) {
			System.out.println("Element Not Found:" + strElementName + ":" + UIDriver.getElementProperty(strElementName)
					.replace("#param1#", sParam1).replace("#param2#", sParam2));
		}
		return bElementPresent;

	}

	public static void wait(int iTimeinSeconds) {
		try {
			Thread.sleep(iTimeinSeconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getElementProperty(String strElementName) {

		String strElementProperty = "";
		for (HashMap<String, String> objORRow : UIDriver.OR) {
			String strElementNameOR = objORRow.get("elementName");
			if (strElementNameOR.equalsIgnoreCase(strElementName)) {
				strElementProperty = objORRow.get("Property");
				break;
			}

		}
		if (strElementProperty.equals("")) {
			System.out.println("Element Name Not Found in Repository : " + strElementName);
		}
		return strElementProperty;

	}

	public static String getExpectedElementValue(String strElementName) {

		String strElementProperty = "";
		for (HashMap<String, String> objORRow : UIDriver.OR) {
			String strElementNameOR = objORRow.get("elementName");
			if (strElementNameOR.equalsIgnoreCase(strElementName)) {
				strElementProperty = objORRow.get("ExpectedValue");
				break;
			}

		}
		if (strElementProperty.equals("")) {
			System.out.println("Element Name Not Found in Repository : " + strElementName);
		}
		return strElementProperty;

	}

	public static void launchURL(String strURL) {
		// WebDriver driver = getDriver();
		System.out.println("browser driver is " + BrowserDriver.driver);
		// driver.get(strURL);
		BrowserDriver.driver.get(strURL);
	}

	public static List<WebElement> getElementsByProperty(String strProperty) {
		// String strProperty=UIDriver.getElementProperty(strElementName);

		List<WebElement> objElements;

		objElements = BrowserDriver.driver.findElements(By.id(strProperty));

		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.id(strProperty));
		}

		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.xpath(strProperty));
		}
		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.name(strProperty));
		}
		return objElements;
	}

	public static WebElement getElementByProperty(String strProperty) {
		// String strProperty=UIDriver.getElementProperty(strElementName);

		if (strProperty == "") {
			return null;
		}

		List<WebElement> objElements;

		objElements = BrowserDriver.driver.findElements(By.id(strProperty));

		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.xpath(strProperty));
		}
		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.name(strProperty));
		}
		if (objElements.size() > 0) {
			return objElements.get(0);
		} else {
			return null;
		}
	}

	public static WebElement getElementDynamic(String strElementName, String sParamValue) {
		String strProperty = UIDriver.getElementProperty(strElementName);

		strProperty = strProperty.replace("#param#", sParamValue);

		if (strProperty == "") {
			return null;
		}

		List<WebElement> objElements;

		objElements = BrowserDriver.driver.findElements(By.id(strProperty));

		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.xpath(strProperty));
		}
		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.name(strProperty));
		}
		if (objElements.size() > 0) {
			return objElements.get(0);
		} else {
			// System.out.println("Element NOT Found:"+strElementName +":" +strProperty);
			return null;
		}
	}

	public static WebElement getElementDynamic(String strElementName, String sParam1, String sParam2) {
		String strProperty = UIDriver.getElementProperty(strElementName);

		strProperty = strProperty.replace("#param1#", sParam1);
		strProperty = strProperty.replace("#param2#", sParam2);

		if (strProperty == "") {
			return null;
		}

		List<WebElement> objElements;

		objElements = BrowserDriver.driver.findElements(By.id(strProperty));

		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.xpath(strProperty));
		}
		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.name(strProperty));
		}
		if (objElements.size() > 0) {
			return objElements.get(0);
		} else {
			// System.out.println("Element NOT Found:"+strElementName +":" +strProperty);
			return null;
		}
	}

	public static WebElement getElement(String strElementName) {
		String strProperty = UIDriver.getElementProperty(strElementName);

		if (strProperty == "") {
			return null;
		}

		List<WebElement> objElements;
		WebDriverWait wait = new WebDriverWait(BrowserDriver.driver, 5);

		objElements = BrowserDriver.driver.findElements(By.id(strProperty));

		if (objElements.size() == 0) {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strProperty)));
			objElements = BrowserDriver.driver.findElements(By.xpath(strProperty));
		}
		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.name(strProperty));
		}
		if (objElements.size() > 0) {
			return objElements.get(0);
		} else {
			return null;
		}
	}

	public static List<WebElement> getElements(String strElementName, String sParam) {
		String strProperty = UIDriver.getElementProperty(strElementName);

		strProperty = strProperty.replace("#param#", sParam);

		if (strProperty == "") {
			return null;
		}

		List<WebElement> objElements;

		objElements = BrowserDriver.driver.findElements(By.id(strProperty));

		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.xpath(strProperty));
		}
		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.name(strProperty));
		}
		if (objElements.size() > 0) {
			return objElements;
		} else {
			return null;
		}
	}

	public static List<WebElement> getElements(String strElementName) {
		String strProperty = UIDriver.getElementProperty(strElementName);

		if (strProperty == "") {
			return null;
		}

		List<WebElement> objElements;
		WebDriverWait wait = new WebDriverWait(BrowserDriver.driver, 5);
		objElements = BrowserDriver.driver.findElements(By.id(strProperty));

		if (objElements.size() == 0) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strProperty)));
			objElements = BrowserDriver.driver.findElements(By.xpath(strProperty));
		}
		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.name(strProperty));
		}
		if (objElements.size() > 0) {
			return objElements;
		} else {
			return null;
		}
	}

	public static void scrollToElement(String strElement) {
		((JavascriptExecutor) BrowserDriver.driver)
				.executeScript("window.scrollTo(0," + UIDriver.getElement(strElement).getLocation().y + ")");
		if (CSVReporter.bDetail == true) {
			CSVReporter.reportAll("Done", "Scroll to Element", "Screen scolled to " + strElement, "", "");
		}
	}

	public static void scrollToElement(WebElement oElement) {
		((JavascriptExecutor) BrowserDriver.driver)
				.executeScript("window.scrollTo(0," + oElement.getLocation().y + ")");
		if (CSVReporter.bDetail == true) {
			// CSVReporter.reportAll("Done", "Scroll to Element", "Screen scolled to
			// "+oElement.getText(), "", "");
		}
	}

	public static void setTextToElement(String strElement, String sText) {
		JavascriptExecutor executor = (JavascriptExecutor) BrowserDriver.driver;
		executor.executeScript("arguments[0].innerHTML = arguments[1]", UIDriver.getElement(strElement), sText);
		if (CSVReporter.bDetail == true) {
			CSVReporter.reportAll("Done", "Set Element Text", strElement + " element text is set  " + sText, "", "");
		}

	}

	public static void setTextToElement(WebElement oElement, String sText) {
		JavascriptExecutor executor = (JavascriptExecutor) BrowserDriver.driver;
		executor.executeScript("arguments[0].innerHTML = arguments[1]", oElement, sText);
		if (CSVReporter.bDetail == true) {
			CSVReporter.reportAll("Done", "Set Element Text", " element text is set  " + sText, "", "");
		}

	}

	public static void clickElementJS(String strElement) {
		JavascriptExecutor executor = (JavascriptExecutor) BrowserDriver.driver;
		executor.executeScript("arguments[0].click();", UIDriver.getElement(strElement));
		UIDriver.wait(2);
		if (CSVReporter.bDetail == true) {
			CSVReporter.reportAll("Done", "Element clicked", strElement, "", "");
		}

	}

	public static void clickElementJS(String strElement, String sParam) {
		JavascriptExecutor executor = (JavascriptExecutor) BrowserDriver.driver;
		executor.executeScript("arguments[0].click();", UIDriver.getElementDynamic(strElement, sParam));
		UIDriver.wait(2);
		if (CSVReporter.bDetail == true) {
			CSVReporter.reportAll("Done", "Element clicked", strElement, "", "");
		}

	}

	public static void scrollToDynamicElement(String strElementName, String sParamValue) {
		((JavascriptExecutor) BrowserDriver.driver).executeScript(
				"window.scrollTo(0," + UIDriver.getElementDynamic(strElementName, sParamValue).getLocation().y + ")");
	}

	public static String getElementAttribute(String strElementName, String strAttribute) {
		String strProperty = UIDriver.getElementProperty(strElementName);

		if (strProperty == "") {
			return "";
		}

		if (UIDriver.checkElementpresentDOM(strElementName, 5) == false) {
			System.out.println("Element Not Found:" + strElementName + " : '" + strProperty + "'");
			CSVReporter.reportFail("Validation for Element", strElementName, "Element Not Present");
			return "";
		}

		List<WebElement> objElements;

		objElements = BrowserDriver.driver.findElements(By.id(strProperty));

		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.xpath(strProperty));
		}
		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.name(strProperty));
		}
		if (objElements.size() > 0) {
			return objElements.get(0).getAttribute(strAttribute);
		} else {
			return "";
		}
	}

	public static String getElementAttribute(String strElementName, String sParam, String strAttribute) {
		String strProperty = UIDriver.getElementProperty(strElementName);

		if (strProperty == "") {
			return "";
		}
		strProperty = strProperty.replace("#param#", sParam);

		if (UIDriver.checkElementpresentDOM(strElementName, sParam, 5) == false) {
			System.out.println("Element Not Found:" + strElementName + " : '" + strProperty + "'");
			CSVReporter.reportFail("Validation for Element", strElementName, "Element Not Present");
			return "";
		}

		List<WebElement> objElements;

		objElements = BrowserDriver.driver.findElements(By.id(strProperty));

		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.xpath(strProperty));
		}
		if (objElements.size() == 0) {
			objElements = BrowserDriver.driver.findElements(By.name(strProperty));
		}
		if (objElements.size() > 0) {
			return objElements.get(0).getAttribute(strAttribute);
		} else {
			return "";
		}
	}

	public static void clickElementDynamic(String strElementName, String sParamValue) {

		if (UIDriver.checkElementpresentDynamic(strElementName, sParamValue, 15)) {
			WebElement oElement = UIDriver.getElementDynamic(strElementName, sParamValue);
			try {
				oElement.click();
			} catch (Exception e) {
				System.out.println(
						"Clicking Element through JS:" + strElementName + ":" + getElementProperty(strElementName));
				clickElementJS(strElementName, sParamValue);
			}

			wait(4);
		} else {

		}
		if (CSVReporter.bDetail == true) {
			CSVReporter.reportAll("Done", "Element clicked", strElementName, "", "");
		}
	}

	public static void switchToTab(int iWindow) {
		ArrayList<String> allTabs = new ArrayList<String>(BrowserDriver.driver.getWindowHandles());
		if (allTabs.size() >= iWindow) {
			BrowserDriver.driver.switchTo().window(allTabs.get(iWindow));

		} else {
			System.out.println("Window not Found:" + iWindow);
		}
		if (CSVReporter.bDetail == true) {
			CSVReporter.reportAll("Done", "Switch To Tab:" + iWindow, "Window Switched", "", "");
		}
	}

	public static void switchToFrame(String sAttribute, String sAttributeValue) {
		List<WebElement> oFrames = UIDriver.getElementsByProperty("//iframe");

		int iFrame = 0;

		for (WebElement oFrame : oFrames) {
			String sTitle = oFrame.getAttribute(sAttribute);
			if (sTitle.contains(sAttributeValue)) {
				BrowserDriver.driver.switchTo().frame(oFrame);
				iFrame++;
				break;
			}

		}
		if (iFrame == 0) {
			System.out.println("Frame NOT Found:" + sAttribute + "=" + sAttributeValue);
		}

	}

	public static void closeTab(int iWindow) {
		ArrayList<String> allTabs = new ArrayList<String>(BrowserDriver.driver.getWindowHandles());
		if (allTabs.size() >= iWindow) {
			BrowserDriver.driver.switchTo().window(allTabs.get(iWindow)).close();
			;
		} else {
			System.out.println("Window not Found:" + iWindow);
		}
	}

	public static boolean clickLinkByText(String linkText) {
		List<WebElement> linkElement = BrowserDriver.driver.findElements(By.linkText(linkText));
		if (linkElement.size() > 0) {
			BrowserDriver.driver.findElement(By.linkText(linkText));
			return true;
		}
		return false;
	}

	public static void clickElement(String strElementName) {
		String strProperty = UIDriver.getElementProperty(strElementName);

		if (strProperty == "") {
			return;
		}

		if (UIDriver.checkElementpresent(strElementName, 7) == false) {
			CSVReporter.reportFail("Validation for Element", strElementName, "Element Not Present");
			return;
		}

		WebElement objElement = UIDriver.getElementByProperty(strProperty);

		if (objElement.isEnabled()) {

			try {
				try {
					objElement.click();
				} catch (Exception e) {
					System.out.println(
							"Clicking Element through JS:" + strElementName + ":" + getElementProperty(strElementName));
					clickElementJS(strElementName);
				}

				if (CSVReporter.bDetail == true) {
					CSVReporter.reportAll("Done", "Click Element", strElementName, "", "");
				}
				wait(3);
			} catch (Exception e) {
				System.out.println("Element Not Clickable:" + e.toString());
			}
		} else {
			System.out.println("Failed to Click Element : '" + strElementName + "' : Element is disabled");
		}
	}

	public static void clickElementWithoutProperty(String xPath) {

		WebElement objElement = BrowserDriver.driver.findElement(By.xpath(xPath));
		System.err.println("printing the element " + objElement);
		if (objElement.isEnabled()) {

			try {
				try {
					objElement.click();
				} catch (Exception e) {
					System.out.println("Clicking Element :" + xPath);

				}

				wait(3);
			} catch (Exception e) {
				System.out.println("Element Not Clickable:" + e.toString());
			}
		}
	}

	public static boolean checkElementPresentWithoutProperty(String xPath) {

		List<WebElement> objElement = UIDriver.getElementsByProperty(xPath);

		if (!objElement.isEmpty()) {
			return true;
		}
		return false;

	}

	public static void setValue(String strElementName, String strValue) {
		String strProperty = UIDriver.getElementProperty(strElementName);
		if (strProperty == "") {
			System.out.println("verify Element in OR " + strElementName);
			return;
		}

		if (strValue.equals("") || strValue == null) {
			System.out.println("No Value to set in " + strElementName + ":" + strProperty);
			return;
		}

		if (strValue.equalsIgnoreCase("blank")) {
			strValue = "";
		}

		if (UIDriver.checkElementpresent(strElementName, 5) == false) {
			System.out.println("Element Not Found:" + strElementName + " : '" + strProperty + "'");
			CSVReporter.reportFail("Validation for Element", strElementName, "Element Not Present");
		}

		WebElement objElement = UIDriver.getElementByProperty(strProperty);

		if (objElement.isEnabled()) {
			objElement.clear();
			objElement.sendKeys(strValue);
			if (strElementName.equalsIgnoreCase("password")) {
				strValue = "";
			}
			if (CSVReporter.bDetail == true) {
				CSVReporter.reportAll("Done", "Set value in Element:" + strElementName, "Value set:" + strValue, "",
						"");
			}
		} else {
			System.out.println("Failed to Set value in Element : '" + strElementName + "' : Element is disabled");
		}
	}

	public static void setValue(String strElementName, String sParam, String strValue) {
		String strProperty = UIDriver.getElementProperty(strElementName);
		strProperty = strProperty.replace("#param#", sParam);

		if (strValue.equals("") || strValue == null) {
			System.out.println("No Value to set in " + strElementName + ":" + strProperty);
			return;
		}

		if (strValue.equalsIgnoreCase("blank")) {
			strValue = "";
		}

		if (strProperty == "") {
			System.out.println("verify Element in OR " + strElementName);
			return;
		}

		if (UIDriver.checkElementpresentDynamic(strElementName, sParam, 5) == false) {
			System.out.println("Element Not Found:" + strElementName + " : '" + strProperty + "'");
			CSVReporter.reportFail("Validation for Element", strElementName, "Element Not Present");
			return;
		}

		WebElement objElement = UIDriver.getElementByProperty(strProperty);

		if (objElement.isEnabled()) {
			objElement.clear();
			objElement.sendKeys(strValue);
			if (strElementName.equalsIgnoreCase("password")) {
				strValue = "";
			}
			if (CSVReporter.bDetail == true) {
				CSVReporter.reportAll("Done", "Set value in Element:" + strElementName, "Value set:" + strValue, "",
						"");
			}
		} else {
			System.out.println("Failed to Set value in Element : '" + strElementName + "' : Element is disabled");
		}
	}

	public static String getElementText(String strElementName) {
		if (UIDriver.checkElementpresent(strElementName, 5) == false) {
			System.out.println("Element NOT Found:" + strElementName);
			return "";
		}
		WebElement objElement = UIDriver.getElement(strElementName);
		return objElement.getText();
	}

	public static String getElementOuterHTML(String strElementName) {
		if (UIDriver.checkElementpresent(strElementName, 5) == false) {
			System.out.println("Element NOT Found:" + strElementName);
			return "";
		}
		WebElement objElement = UIDriver.getElement(strElementName);
		return objElement.getAttribute("outerHTML");
	}

	public static String getOuterHTMLForXPath(String xpath) {
		String outerHTML = null;
		WebElement objElement = BrowserDriver.driver.findElement(By.xpath(xpath));
		System.err.println("printing the element " + objElement);
		try {
			outerHTML = objElement.getAttribute("outerHTML");
			wait(3);
		} catch (Exception e) {
			System.out.println("Element Not Found:" + e.toString());
		}

		return outerHTML;
	}

	public static String getElementClass(String strElementName) {
		if (UIDriver.checkElementpresent(strElementName, 5) == false) {
			System.out.println("Element NOT Found:" + strElementName);
			return "";
		}
		WebElement objElement = UIDriver.getElement(strElementName);
		return objElement.getAttribute("class");
	}

	public static String getElementId(String strElementName) {
		if (UIDriver.checkElementpresent(strElementName, 5) == false) {
			System.out.println("Element NOT Found:" + strElementName);
			return "";
		}
		WebElement objElement = UIDriver.getElement(strElementName);
		return objElement.getAttribute("id");
	}

	public static String getElementText(String strElementName, String sParam) {
		if (UIDriver.checkElementpresentDynamic(strElementName, sParam, 5) == false) {
			System.out.println("Element NOT Found:" + strElementName);
			return "";
		}
		WebElement objElement = UIDriver.getElementDynamic(strElementName, sParam);
		return objElement.getText();
	}

	public static void validateElementwithText(String sText) {
		String sXPath = "//*[text()='" + sText + "']";

		if (sText.equals("") || sText == null) {
			System.out.println("Text Not Given for Element:");
			return;
		}

		try {
			List<WebElement> objElements = getElementsByProperty(sXPath);
			boolean bEle = false;
			if (objElements == null) {
				bEle = false;
			} else {
				bEle = (objElements.size() > 0);
			}

			CSVReporter.reportPassFail(bEle, "Validation for Element with Text:" + sText, "Element should be present",
					"Element is Present", "Element is NOT Present");
		} catch (Exception e) {
			CSVReporter.reportFail("Validation for Element with Text:" + sText, "Element should be present",
					"Element is NOT Present");
			System.out.println("Element with Text '" + sText + "' is NOT present");
		}

	}

	public static void selectValue(String strElementName, String strValue, String strBy) {
		String strProperty = UIDriver.getElementProperty(strElementName);
		if (strProperty == "") {
			System.out.println("verify Element in OR " + strElementName);
			return;
		}

		if (UIDriver.checkElementpresent(strElementName, 5) == false) {
			System.out.println("Element Not Found:" + strElementName + " : '" + strProperty + "'");
			CSVReporter.reportFail("Validation for Element", strElementName, "Element Not Present");
		}

		WebElement objElement = UIDriver.getElementByProperty(strProperty);
		Select objSelect = new Select(objElement);

		if (objElement.isEnabled()) {
			if (strBy.equalsIgnoreCase("index")) {
				objSelect.selectByIndex(Integer.parseInt(strValue));
			} else if (strBy.equalsIgnoreCase("value")) {
				objSelect.selectByValue(strValue);
			} else if (strBy.equalsIgnoreCase("text")) {
				objSelect.selectByVisibleText(strValue);
			}
			if (CSVReporter.bDetail == true) {
				CSVReporter.reportAll("Done", "Select value in Element:" + strElementName, "Value set:" + strValue, "",
						"");
			}

		} else {
			System.out.println("Failed to Selected value in Element : '" + strElementName + "' : Element is disabled");
		}
	}

	public static List<String> getSelectValues(String strElementName) {
		String strProperty = UIDriver.getElementProperty(strElementName);
		List<String> selectOptions = new ArrayList<String>();
		if (strProperty == "") {
			System.out.println("verify Element in OR " + strElementName);
			return null;
		}

		if (UIDriver.checkElementpresent(strElementName, 5) == false) {
			System.out.println("Element Not Found:" + strElementName + " : '" + strProperty + "'");
			CSVReporter.reportFail("Validation for Element", strElementName, "Element Not Present");
		}

		WebElement objElement = UIDriver.getElementByProperty(strProperty);
		Select select = new Select(objElement);
		List<WebElement> options = select.getOptions();

		for (WebElement option : options) {
			selectOptions.add(option.getText());
		}

		return selectOptions;
	}

	public static boolean checkImageExists(String strElementName) {

		boolean isLoaded = false;
		String strProperty = UIDriver.getElementProperty(strElementName);

		if (strProperty == "") {
			System.out.println("verify Element in OR " + strElementName);
			return false;
		}

		if (UIDriver.checkElementpresent(strElementName, 5) == false) {
			System.out.println("Element Not Found:" + strElementName + " : '" + strProperty + "'");
			CSVReporter.reportFail("Validation for Element", strElementName, "Element Not Present");
		}

		WebElement imageElement = UIDriver.getElementByProperty(strProperty);
		Object result = ((JavascriptExecutor) BrowserDriver.driver).executeScript("return arguments[0].complete && "
						+ "typeof arguments[0].naturalWidth != \"undefined\" && " + "arguments[0].naturalWidth > 0",
				imageElement);

		if (result instanceof Boolean) {
			isLoaded = (Boolean) result;
		}
		return isLoaded;
	}

	public static boolean checkIfAllProductImageExists(String strElementName) {

		boolean isLoaded = false;
		String strProperty = UIDriver.getElementProperty(strElementName);

		if (strProperty == "") {
			System.out.println("verify Element in OR " + strElementName);
			return false;
		}

		if (UIDriver.checkElementpresent(strElementName, 5) == false) {
			System.out.println("Element Not Found:" + strElementName + " : '" + strProperty + "'");
			CSVReporter.reportFail("Validation for Element", strElementName, "Element Not Present");
		}

		List<WebElement> imageElements = UIDriver.getElementsByProperty(strProperty);

		for (WebElement imageElement : imageElements) {
			Object result = ((JavascriptExecutor) BrowserDriver.driver).executeScript("return arguments[0].complete && "
							+ "typeof arguments[0].naturalWidth != \"undefined\" && " + "arguments[0].naturalWidth > 0",
					imageElement);
			if (result instanceof Boolean) {
				isLoaded = (Boolean) result;
			}
			if (!isLoaded)
				return false;
		}

		return isLoaded;
	}

	public static boolean isButtonEnabled(String strElementName) {
		String strProperty = UIDriver.getElementProperty(strElementName);
		if (strProperty == "") {
			return false;
		}

		if (UIDriver.checkElementpresent(strElementName, 5) == false) {
			System.out.println("Element Not Found:" + strElementName + " : '" + strProperty + "'");
			CSVReporter.reportFail("Validation for Element", strElementName, "Element Not Present");
		}

		WebElement objElement = UIDriver.getElementByProperty(strProperty);
		return objElement.isEnabled();
	}

	public static void selectCheckbox(String strElementName) {
		WebElement oCheck = UIDriver.getElement(strElementName);
		if (oCheck != null) {
			if (oCheck.isSelected() == false) {
				oCheck.click();
			}
		}
	}

	public static void selectValue(String strElementName, String sParam, String strValue, String strBy) {
		String strProperty = UIDriver.getElementProperty(strElementName);
		strProperty = strProperty.replace("#param#", sParam);
		if (strProperty == "") {
			System.out.println("verify Element in OR " + strElementName);
			return;
		}

		if (UIDriver.checkElementpresentDynamic(strElementName, sParam, 5) == false) {
			System.out.println("Element Not Found:" + strElementName + " : '" + strProperty + "'");
			CSVReporter.reportFail("Validation for Element", strElementName, "Element Not Present");
		}

		WebElement objElement = UIDriver.getElementByProperty(strProperty);
		Select objSelect = new Select(objElement);

		if (objElement.isEnabled()) {
			if (strBy.equalsIgnoreCase("index")) {
				objSelect.selectByIndex(Integer.parseInt(strValue));
			} else if (strBy.equalsIgnoreCase("value")) {
				objSelect.selectByValue(strValue);
			} else if (strBy.equalsIgnoreCase("text")) {
				objSelect.selectByVisibleText(strValue);
			}
			if (CSVReporter.bDetail == true) {
				CSVReporter.reportAll("Done", "Select value in Element:" + strElementName, "Value set:" + strValue, "",
						"");
			}

		} else {
			System.out.println("Failed to Selected value in Element : '" + strElementName + "' : Element is disabled");
		}
	}

	public static void goBack() {
		BrowserDriver.driver.navigate().back();
	}

	public static boolean checkIfCartDataSameAsAddedProducts() {

		return true;
	}

	public static boolean pagination() {
		try {
			WebElement paginationDiv = UIDriver.getElement("paginationLinksDiv");
			List<WebElement> paginationLinks = paginationDiv
					.findElements(By.xpath(UIDriver.getElementProperty("paginationLink")));
			int numberOfPaginationLinks = paginationLinks.size();
			if (numberOfPaginationLinks > 0) {

				for (int i = 0; i < numberOfPaginationLinks; i++) {
					paginationDiv = UIDriver.getElement("paginationLinksDiv");
					paginationLinks = paginationDiv
							.findElements(By.xpath(UIDriver.getElementProperty("paginationLink")));
					Thread.sleep(3000);
					paginationLinks.get(i).click();
					BrowserDriver.driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
				}
			}
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyToolTip(String strPropertyForTooltip, String strPropertyForTooltipText) {

		WebElement element = UIDriver.getElement(strPropertyForTooltip);
		Actions toolAct = new Actions(BrowserDriver.driver);
		toolAct.moveToElement(element).build().perform();
		WebElement toolTipElement = UIDriver.getElement(strPropertyForTooltipText);
		String toolTipText = toolTipElement.getText();
		System.out.println(toolTipText);
		return !toolTipText.isEmpty();
	}

	public static List<WebElement> getElements(WebElement weElement, String strProperty, String byMethod) {
		List<WebElement> objElements = null;
		switch (byMethod) {
			case "id":
				objElements = weElement.findElements(By.id(strProperty));
				break;
			case "className":
				objElements = weElement.findElements(By.className(strProperty));
				break;
			case "tagName":
				objElements = weElement.findElements(By.tagName(strProperty));
				break;
			case "linkText":
				objElements = weElement.findElements(By.linkText(strProperty));
				break;

		}

		return objElements;
	}

	public static void setValueWithoutProperty(String strProperty, String strValue) {
		if (strProperty == "") {
			System.out.println("verify Element in OR " + strProperty);
			return;
		}

		if (strValue.equalsIgnoreCase("blank")) {
			strValue = "";
		}

		if (UIDriver.checkElementPresentWithoutProperty(strProperty) == false) {
			System.out.println("Element Not Found:" + " : '" + strProperty + "'");
			CSVReporter.reportFail("Validation for Element", strProperty, "Element Not Present");
		}

		WebElement objElement = UIDriver.getElementByProperty(strProperty);

		if (objElement.isEnabled()) {
			objElement.clear();
			objElement.sendKeys(strValue);

			if (CSVReporter.bDetail == true) {
				CSVReporter.reportAll("Done", "Set value in Element:" + strProperty, "Value set:" + strValue, "",
						"");
			}
		} else {
			System.out.println("Failed to Set value in Element : '" + strProperty + "' : Element is disabled");
		}
	}
}