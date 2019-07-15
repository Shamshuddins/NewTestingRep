package main.java.framework;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class XMLDriver {
	public static Document doc;
	public static Attr attr;
	public static Element rootElement;
	public static Element staff;

	public static void createTestNGXML(String appName) {

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			// root elements
			doc = docBuilder.newDocument();
			rootElement = doc.createElement("suite");
			attr = doc.createAttribute("name");
			attr.setValue("AutomationSuite");
			rootElement.setAttributeNode(attr);
			doc.appendChild(rootElement);
			//doc.append("<listeners><listener class-name = \"CustomReporter\" /></listeners>");
			DOMImplementation domImpl = doc.getImplementation();
			DocumentType doctype = domImpl.createDocumentType("doctype", "-//Oberon//YOUR PUBLIC DOCTYPE//EN",
					"http://testng.org/testng-1.0.dtd");

			List<HashMap<String, String>> arrTestSet = null;
			List<HashMap<String, String>> arrTestCase = null;
			try {
				arrTestSet = Framework.removeRecordIfNo(CSV_IO.readCSVFile(TestDriver.testSetPath));
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			//Element oListeners = addElement("listeners", "", "", rootElement);
			//Element oListener = addElement("listener", "class-name", "CustomReporter", oListeners);
			for (HashMap<String, String> oTestSet : arrTestSet) {
				String sTestCase = oTestSet.get("TestCase");
				Element oTest1 = addElement("test", "name", sTestCase, rootElement);
				Element oClasses1 = addElement("classes", "", "", oTest1);
				Element oClass1 = addElement("class", "name", "main.java.module." + appName, oClasses1);
				Element oMethods1 = addElement("methods", "", "", oClass1);
				String strTestCaseFile = TestDriver.folderpath + "//Testcases//" + sTestCase + ".csv";
				try {
					arrTestCase = Framework.removeRecordIfNo(CSV_IO.readCSVFile(strTestCaseFile));
				} catch (IOException e) {
					e.printStackTrace();
				}
				String sPreviousComponentName = "";
				int iRepeat = 1;
				for (HashMap<String, String> oTestCase : arrTestCase) {
					String strComponentName = Utility.getinputvalue(oTestCase, "componentName");
					if (sPreviousComponentName.contains(strComponentName)) {
						sPreviousComponentName = "";
						oTest1 = addElement("test", "name", sTestCase + "_" + iRepeat, rootElement);
						oClasses1 = addElement("classes", "", "", oTest1);
						oClass1 = addElement("class", "name", "main.java.module." + appName, oClasses1);
						oMethods1 = addElement("methods", "", "", oClass1);
						iRepeat++;

					}
					String strDataRowID = Utility.getinputvalue(oTestCase, "dataRowID");
					HashMap<String, String> oInput = Framework.getDataRecord(strDataRowID, strComponentName);
					TestDriver.input = oInput;
					Element oComponent = addElement("include", "name", strComponentName, oMethods1);
					Element oParameter = addElement("parameter", "name", "DataRowID", oComponent);
					oParameter.setAttribute("value", strDataRowID);
					sPreviousComponentName = sPreviousComponentName + ";" + strComponentName;

				}
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			String sXMLFilePath = "testng.xml";
			File oFile = new File(sXMLFilePath);
			try {
				oFile.createNewFile();

			} catch (IOException e) {
				e.printStackTrace();
			}
			StreamResult result = new StreamResult(oFile.getAbsolutePath());
			transformer.transform(source, result);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public static Element addElement(String sElementName, String sAttribute, String sAttributeValue, Element oElement) {
		staff = doc.createElement(sElementName);
		if (sAttribute != "") {
			attr = doc.createAttribute(sAttribute);
			attr.setValue(sAttributeValue);
			staff.setAttributeNode(attr);
		}
		oElement.appendChild(staff);
		return staff;
	}
}
