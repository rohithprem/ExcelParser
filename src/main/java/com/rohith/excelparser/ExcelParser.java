package com.rohith.excelparser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser {

	private static int invalid = 0;
	private static int totalInvalid = 0; 

	public static void main(String[] args) throws InvalidFormatException, IOException {
		XSSFWorkbook wb = new XSSFWorkbook(new File("/Users/CS93/Downloads/Food Providers for Mobile.xlsx"));
		System.out.println("No of Sheets: " + wb.getNumberOfSheets());
		int sheets = wb.getNumberOfSheets();
		List<String> allPhoneNumbers = new ArrayList<>();
		for(int sheetIndex = 0 ; sheetIndex < 1 ; sheetIndex++) {
			List<String> phoneNumbersInSheet = new ArrayList<String>();
			totalInvalid += invalid;
			invalid = 0;
			XSSFSheet sheet = wb.getSheetAt(sheetIndex);
			System.out.println("\n\nSheet: " + sheet.getSheetName());
			int rowIndex = 0;
			int maxRows = sheet.getPhysicalNumberOfRows();
			for (; rowIndex < maxRows; rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex);
				if (row != null) {
					XSSFCell cell = row.getCell(2);
					if (cell != null) {
						CellType cellType = cell.getCellType();
						Double number = null;
						String value = "";
						if (cellType.equals(CellType.NUMERIC)) {
							number = cell.getNumericCellValue();
						} else {
							value = cell.getStringCellValue();
						}
						if (number != null) {
							value = String.format("%.0f", number);
							phoneNumbersInSheet.add(value);
						} else if (!value.isEmpty()) {
//						System.out.println("VAl: " + value);
							List<String> parsedNumbers = parsePhoneNumber(value);
							phoneNumbersInSheet.addAll(parsedNumbers);
						}
						
					}
				}
				
			}
			System.out.println("\nSuccess: " + phoneNumbersInSheet.size());
			System.out.println("Invalid: " + invalid);
			allPhoneNumbers.addAll(phoneNumbersInSheet);
		}
		
		File contacts = new File("contacts_foodprovidermobile.txt");
		System.out.println(contacts.getAbsolutePath());
		contacts.createNewFile();
		FileWriter writer = new FileWriter(contacts);
		for (String phoneNumber : allPhoneNumbers) {
			writer.write("91" + phoneNumber + System.lineSeparator());
		}
		writer.close();
		System.out.println("SUCCESS! : " + allPhoneNumbers.size());
		System.out.println("INVALID: " + totalInvalid);
	}

	public static List<String> parsePhoneNumber(String value) {
		List<String> phoneNumbers = new ArrayList<>();
		if (value.contains("/")) {

			phoneNumbers.addAll(valueWithSeparator(value, "/"));

		} else if (value.contains("-")) {

			phoneNumbers.addAll(valueWithSeparator(value, "-"));

		} else if (value.contains("&")) {

			phoneNumbers.addAll(valueWithSeparator(value, "&"));

		} else if (value.contains("\n")) {

			phoneNumbers.addAll(valueWithSeparator(value, "\n"));

		} else if (value.contains(",")) {

			phoneNumbers.addAll(valueWithSeparator(value, ","));

		} else if (value.trim().startsWith("+91")) {

			phoneNumbers.addAll(valueStartingWithPlus(value));

		} else if (value.trim().contains(" ")) {

			phoneNumbers.addAll(valuesWithSpaces(value));

		} else {

			System.out.println("INVALID: " + value);
			invalid++;
		}
		return phoneNumbers;
	}

	private static List<String> valuesWithSpaces(String value) {
		List<String> phoneNumbers = new ArrayList<>();
		String phoneNumber = "";
		String[] separatedNumbers = value.trim().split(" ");
		for (String separatedNumber : separatedNumbers) {
			if (!StringUtils.isNumeric(separatedNumber)) {
				System.out.println("INVALID: " + separatedNumber);
				continue;
			}
//			System.out.println("Adding: " + separatedNumber);
			phoneNumber += separatedNumber;
			if (phoneNumber.length() == 10) {
//				System.out.println("Compete! : " + phoneNumber);
				phoneNumbers.add(phoneNumber);
				phoneNumber = new String();
			}
		}
		return phoneNumbers;
	}

	private static List<String> valueStartingWithPlus(String value) {
		List<String> phoneNumbers = new ArrayList<>();
//		System.out.println("+:" + value);
		value = value.trim().substring(3);
//		System.out.println("+:" + value);
		if (StringUtils.isNumeric(value.trim()) && value.trim().length() == 10) {
			phoneNumbers.add(value.trim());
		} else {
			List<String> parsedNumbers = parsePhoneNumber(value.trim());
			phoneNumbers.addAll(parsedNumbers);
		}
		return phoneNumbers;
	}

	private static List<String> valueWithSeparator(String value, String separator) {
		List<String> phoneNumbers = new ArrayList<>();
		String[] possibleNumbers = value.split(separator);
		for (String possibleNumber : possibleNumbers) {
			if (StringUtils.isNumeric(possibleNumber.trim()) && possibleNumber.trim().length() == 10) {
				phoneNumbers.add(possibleNumber.trim());
			} else {
				List<String> parsedNumbers = parsePhoneNumber(possibleNumber.trim());
				phoneNumbers.addAll(parsedNumbers);
			}

		}
		return phoneNumbers;
	}

}
