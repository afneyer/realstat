package com.afn.realstat;

import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvFileWriter {

	private FileWriter fileWriter;
	private String header;
	private long row;
	private long column;
	private long numCols;
	

	private static String LINE_SEP = "\r\n";
	private static String FIELD_SEP = ",";

	public static final Logger importLog = LoggerFactory.getLogger("import");

	public CsvFileWriter(String fileName, String header) {
		this.header = header;
		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter.append(header.toString());
			fileWriter.append(LINE_SEP);

		} catch (Exception e) {
			importLog.error("Error in CsvFileWriter see exception: File=", fileName + "Header = " + this.header);
			e.printStackTrace();
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e1) {
				System.out.println("Error while flushing/closing CsvFileWriter !!!");
				e.printStackTrace();
			}
		}
		row = 1;
		column = 0;
		numCols = header.split(FIELD_SEP).length;
	}

	public void appendLine(String s) {
		if (s.split(FIELD_SEP).length == numCols) {
			try {
				fileWriter.append(s);
				fileWriter.append(LINE_SEP);
			} catch (IOException e) {
				importLog.error("Exception in CsvFileWriter see exception: Line=", row + "Content = " + s);
				close();
			}
		} else {
			importLog.error("Number of columns " + s.split(FIELD_SEP).length + " is different from header " + numCols + " : Line=" + row
					+ "Content = " + s);
		}
	}

	public void close() {
		try {
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error while flushing/closing fileWriter !!!");
			e.printStackTrace();
		}
	}
}
