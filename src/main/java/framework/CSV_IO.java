package main.java.framework;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;

/*import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CSV_IO {

	@SuppressWarnings("null")
	public static List<HashMap<String, String>> readCSVFile(String strFilePath) throws IOException {

		try (Reader reader = Files.newBufferedReader(Paths.get(strFilePath));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);) {
			String[] aHeaders = {};
			int intRow = 0;
			HashMap<String, String> input = new HashMap<>();
			List<HashMap<String, String>> inputs = new ArrayList<HashMap<String, String>>();
			// List<HashMap<String,String>> inputs=null;
			for (CSVRecord csvRecord : csvParser) {

				// System.out.println("Record No - " + csvRecord.getRecordNumber());
				int iColCount = csvRecord.size();

				if (intRow == 0) {
					aHeaders = new String[iColCount];
					for (int intI = 0; intI < iColCount; intI++) {
						aHeaders[intI] = csvRecord.get(intI).toString();
					}
				} else {
					for (int intI = 0; intI < iColCount; intI++) {
						String coValue = csvRecord.get(intI).toString();
						//input.put(aHeaders[intI], coValue);
						if(coValue.length()!=0 && coValue!=null){
							input.put(aHeaders[intI], coValue);
//							System.out.println(aHeaders[intI] +"="+ coValue + "||" + coValue.length());
						}

					}
					@SuppressWarnings("unchecked")
					HashMap<String, String> excelRecord1 = (HashMap<String, String>) input.clone();
					inputs.add(intRow - 1, excelRecord1);

				}
				intRow++;
			}
			return inputs;
		} catch (Exception e) {
			System.out.println("Error while fetching data from " + strFilePath);
			e.printStackTrace();
			
			return null;
		}
	}

	public static void writeinCSV(CSVPrinter csvPrinter, String[] aValues) throws IOException {

		try {
			csvPrinter.printRecord(aValues);
			csvPrinter.flush();
		} catch (Exception e) {
			System.out.println("Error while writing data in CSV Report:" + aValues.toString());
		}
	}

	public static CSVPrinter createNewCSVFile(String sReportPath, String[] aHeaders) {
		try {
            System.out.println("Report path: " + sReportPath);
			//BufferedWriter writer = Files.newBufferedWriter(Paths.get(sReportPath));
            BufferedWriter writer  = Files.newBufferedWriter(Paths.get(sReportPath), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
               
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(aHeaders));
          	writer.flush();
			return csvPrinter;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
