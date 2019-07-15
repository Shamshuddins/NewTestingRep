package main.java.framework;

import java.text.*;
import java.util.*;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import java.util.zip.*;
import java.io.*;
import org.apache.commons.io.FileUtils;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class Utility {

	public static String getinputvalue(HashMap<String, String> input, String sKey) {

		String sInput = "";
		if (input.get(sKey) == null || input.get(sKey).trim().equals("")) {
			sInput = "";
		} else {
			sInput = input.get(sKey).trim();
		}
		return sInput;

	}

	public static boolean ValidateDateSort() {
		List<WebElement> oTimeStamp = UIDriver.getElements("articletimestamp");
		// data-live-timestamp'
		SimpleDateFormat f = new SimpleDateFormat("MMM dd, yyyy");
		String sDate = "";
		ArrayList<Date> oDates = new ArrayList<>();
		ArrayList<Date> oDates1 = new ArrayList<>();
		for (WebElement oElement : oTimeStamp) {
			String sTimeStamp = oElement.getText();

			if (sTimeStamp.toLowerCase().contains("yesterday") == true) {
				f = new SimpleDateFormat("MMM dd, yyyy");
				sDate = f.format(yesterday());

			}

			else if (sTimeStamp.contains("at") == true) {
				f = new SimpleDateFormat("MMM dd, yyyy");
				sDate = sTimeStamp.split("at")[0].trim();
			}

			else {
				String sAttribute = oElement.getAttribute("data-live-timestamp");
				sDate = sAttribute.split("T")[0];
				f = new SimpleDateFormat("yyyy-MM-dd");
			}

			try {
				oDates1.add(f.parse(sDate));
				oDates.add(f.parse(sDate));
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// oDates.add(sDate);
		}

		Collections.sort(oDates1, Collections.reverseOrder());

		boolean bSort = oDates1.equals(oDates);
		CSVReporter.reportPassFail(bSort, "Validation for Articles Order",
				"Articles should be sorted in descending Date order", "Articles visible in correct order",
				"Article NOT shown in correct order");
		return bSort;

	}

	public static Date yesterday() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * @name : stringToList
	 * @author :
	 * @description Convert a string value containing multiple words seperated by a pipe into a list of individual strings
	 * @param strValue string containing multiple words seperated with the pipe delimiter
	 */

	public static List<String> stringToList(String strValue) {
		String[] strAsArray = strValue.split("\\|");
		List<String> strToList = Arrays.asList(strAsArray);
		return strToList;
	}


	/**
	 * @name : stringSpaceToList
	 * @author :
	 * @description Convert a string value containing multiple words seperated by a space into a list of individual strings
	 * @param The string containing multiple words seperated with the space delimiter
	 */

	public static List<String> stringSpaceToList(String strValue) {
		String[] strAsArray = strValue.split("\\ ");
		List<String> strToList = Arrays.asList(strAsArray);
		return strToList;
	}

	/**
	 * @name : listToMapConversion
	 * @author :
	 * @description Convert a list of strings and webelements into a single map with the string as the key
	 * @param string of string values and list of webelements
	 */

	public static LinkedHashMap<String, WebElement> listToMapConversion(List<String> string,
																		List<WebElement> webElement) {

		LinkedHashMap<String, WebElement> entryMap = new LinkedHashMap<String, WebElement>();

		Iterator<String> itr1 = string.iterator();
		Iterator<WebElement> itr2 = webElement.iterator();

		while (itr1.hasNext() && itr2.hasNext()) {
			entryMap.put(itr1.next(), itr2.next());
		}
		return entryMap;
	}

	/**
	 * @name : randomNumberGenerator
	 * @author :
	 * @description Generate a random number between 1 - 99999
	 */

	public static String randomNumberGenerator() {
		Random random = new Random();
		int x = random.nextInt(99999) + 1;
		return Integer.toString(x);
	}

	/**
	 * @name : convertTwoListIntoMap
	 * @author :
	 * @description Convert a list of two strings into a single map
	 * @param list1, list2 lists of Strings
	 */

	public static Map<String, String> convertTwoListIntoMap(List<String> list1, List<String> list2) {
		Map<String, String> tempMap = new LinkedHashMap<String, String>();
		Iterator<String> i1 = list1.iterator();
		Iterator<String> i2 = list2.iterator();
		while (i1.hasNext() && i2.hasNext()) {
			tempMap.put(i1.next(), i2.next());
		}
		return tempMap;
	}

	/**
	 * @name : calculateTotalForProducts
	 * @author :
	 * @description Calculate the total for items in cart/order and return the total amount
	 * @param  priceElements of web elements, where each contains the price text
	 */

	public static String calculateTotalForProducts(List<WebElement> priceElements) {
		double sumOfPrices = 0;
		double productPrice;
		for(WebElement priceElement : priceElements) {
			productPrice = Float.valueOf(priceElement.getText().substring(1).replace("," , ""));
			sumOfPrices += productPrice;
		}
		//sumOfPrices = Math.round(sumOfPrices * 100.0) / 100.0;
		DecimalFormat f = new DecimalFormat("##.00");
		return f.format(sumOfPrices);
	}

	/**
	 * @name : stringToDoubleConversion
	 * @author :
	 * @description Convert an amount, given as String, into a double
	 * @param amount value string
	 */

	public static double stringToDoubleConversion(String amount) {
		return Double.parseDouble(amount.substring(1).replace(",", ""));
	}

	/**
	 * @name : randomDigit
	 * @author :
	 * @description Generate a random number between 1 - 999
	 */

	public static int randomDigit() {
		Random rand = new Random();
		int low = 1;
		int high = 999;
		return(rand.nextInt(high-low) + low);
	}

	/**
	 * @name : randomNumberString
	 * @author :
	 * @description Generate a list of Random numbers (converted to String) for a given limit.
	 * @param limit [number of random numbers to be generated]
	 */

	public static List<String> randomNumberString(int limit){
		return (Lists.transform(new Random().ints(0,350).limit(limit).boxed().collect(Collectors.toList()), Functions.toStringFunction()));
	}

	/**
	 * @name : randomTextGenerator
	 * @author :
	 * @description Generate a random sequence of Strings for a given number of characters.
	 * @param numOfChars of characters
	 */

	public static String randomTextGenerator(int numOfChars) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz  ".toCharArray();
		StringBuilder sb = new StringBuilder(numOfChars);
		Random random = new Random();
		for (int i = 0; i < numOfChars; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		return (output);
	}

	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String strCurrentDateTime = formatter.format(date);
		return strCurrentDateTime;
	}

	public static <T> boolean listEqualsIgnoreOrderIgnoreCase(List<String> list1, List<String> list2) {
		return new HashSet<>(list1.stream()
				.map(String::toLowerCase)
				.collect(Collectors.toList())).equals(new HashSet<>(list2.stream()
				.map(String::toLowerCase)
				.collect(Collectors.toList())));
	}

	public static String capitalizeFirstLetter(String word) {
		return (word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase());
	}

	/*Get all values in a list from a webelement*/
	public static ArrayList<String> webElementList(String webelement, String webElementProperty, String attributeValue) {
		ArrayList getListElementProp = new ArrayList<String>();
		List<WebElement> getListElements = UIDriver.getElementsByProperty(webelement);
		//String webelemementName = UIDriver.getElementProperty(webelement);
		//getListElements = UIDriver.getElements(UIDriver.getElementProperty(webelement));
		for (WebElement listElement : getListElements){
			if (webElementProperty.equalsIgnoreCase("text"))
				getListElementProp.add(listElement.getText().trim());
			if (webElementProperty.equalsIgnoreCase("attribute"))
				getListElementProp.add(listElement.getAttribute(attributeValue));
		}
		return getListElementProp;
	}

	/*Method to intiate sending email*/
	public static void setEmail(String reportSrcFolder, String reportFileName)	{
		String path= reportSrcFolder;	//System.getProperty("projectfolder") + "\\target\\ExecutionReport\\";
		String[] to={"padbandi@coca-cola.com"};
		String[] cc={};
		String[] bcc={};
		Utility.sendMail("autotestko@gmail.com",
				"Pass@1234",
				"smtp.gmail.com",
				"465",
				"true",
				"true",
				true,
				"javax.net.ssl.SSLSocketFactory",
				"false",
				to,
				cc,
				bcc,
				"B2B eCommerce Execution Completed - " + Utility.getCurrentDate(),
				"B2B eCommerce Test Execution Completed",
				path,
				reportFileName);
	}

	/*Method to send email*/
	public static boolean sendMail(String userName,
								   String passWord,
								   String host,
								   String port,
								   String starttls,
								   String auth,
								   boolean debug,
								   String socketFactoryClass,
								   String fallback,
								   String[] to,
								   String[] cc,
								   String[] bcc,
								   String subject,
								   String text,
								   String attachmentPath,
								   String attachmentName){

		//Object Instantiation of a properties file.
		Properties props = new Properties();
		props.put("mail.smtp.user", userName);
		props.put("mail.smtp.host", host);
		if(!"".equals(port)){
			props.put("mail.smtp.port", port);
		}

		if(!"".equals(starttls)){
			props.put("mail.smtp.starttls.enable",starttls);
			props.put("mail.smtp.auth", auth);
		}

		if(debug){
			props.put("mail.smtp.debug", "true");
		}else{
			props.put("mail.smtp.debug", "false");
		}

		if(!"".equals(port)){
			props.put("mail.smtp.socketFactory.port", port);
		}
		if(!"".equals(socketFactoryClass)){
			props.put("mail.smtp.socketFactory.class",socketFactoryClass);
		}
		if(!"".equals(fallback)){
			props.put("mail.smtp.socketFactory.fallback", fallback);
		}

		try{
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(false);
			MimeMessage msg = new MimeMessage(session);
			msg.setText(text);
			msg.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(attachmentPath + attachmentName);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(attachmentName);
			multipart.addBodyPart(messageBodyPart);
			msg.setContent(multipart);
			msg.setFrom(new InternetAddress(userName));

			for(int i=0;i<to.length;i++){
				msg.addRecipient(Message.RecipientType.TO, new
						InternetAddress(to[i]));
			}

			for(int i=0;i<cc.length;i++){
				msg.addRecipient(Message.RecipientType.CC, new
						InternetAddress(cc[i]));
			}

			for(int i=0;i<bcc.length;i++){
				msg.addRecipient(Message.RecipientType.BCC, new
						InternetAddress(bcc[i]));
			}

			msg.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, userName, passWord);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();

			return true;

		} catch (Exception mex){
			mex.printStackTrace();
			return false;
		}
	}

	/* Method to Zip folder */
	public void zipDir(String dirName, String nameZipFile, String ssdir) throws IOException {
		//Copy all Screenshot files to Exeution report folder
		String source = ssdir;
		File srcDir = new File(source);
		String destination = dirName;
		File destDir = new File(destination);
		try {
			FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Zip execution report folder to send through email
		ZipOutputStream zip = null;
		FileOutputStream fW = null;
		nameZipFile += "ExecutionReport.zip";
		fW = new FileOutputStream(nameZipFile);
		zip = new ZipOutputStream(fW);
		addFolderToZip("", dirName, zip);
		zip.close();
		fW.close();
	}

	/* Method to support zipDir. This method gathers files from the source folder */
	private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {
		File folder = new File(srcFolder);
		if (folder.list().length == 0) {
			addFileToZip(path , srcFolder, zip, true);
		}
		else {
			for (String fileName : folder.list()) {
				if (path.equals("")) {
					addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
				}
				else {
					addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false);
				}
			}
		}
	}

	/* Method to support zipDir. This method includes files to zip file */
	private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException {
		File folder = new File(srcFile);
		if (flag) {
			zip.putNextEntry(new ZipEntry(path + "/" +folder.getName() + "/"));
		}
		else {
			if (folder.isDirectory()) {
				addFolderToZip(path, srcFile, zip);
			}
			else {
				byte[] buf = new byte[1024];
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
				while ((len = in.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
			}
		}
	}
//Merge 2 maps, overwrites the value in target if key is found in target,else add it to target.

	public static Map<String, String> mergeLists(Map<String, String> sourceDataMap, Map<String, String> targetDataMap){
		Map<String, String> resultDataMap = null;
		try {
			resultDataMap = new LinkedHashMap(targetDataMap);
			for (String productName : sourceDataMap.keySet()) {
				resultDataMap.put(productName, sourceDataMap.get(productName));
			}
		}
		catch(Exception Ex) {
			System.out.println(Ex.getMessage());
		}
		return resultDataMap;
	}
// get the list of element text for the Xpath provided.
	public static List<String> getElementText(String xpath){
		List<String> itemTextList = new ArrayList<String>();
		List<WebElement> sortListElements = UIDriver.getElements(xpath);
		for (WebElement rowElement : sortListElements) {
			if (!rowElement.getText().equals("") && !rowElement.getText().equals(" ")) {
				itemTextList.add(rowElement.getText());
			}
		}

		return itemTextList;
	}

}