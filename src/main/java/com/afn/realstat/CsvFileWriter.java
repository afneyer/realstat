package com.afn.realstat;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.afn.util.QueryResultTable;

/**
 * The CsvFileWriter generate comma-delimited files for the use in Excel. It
 * takes care of all the internal exception process and throws run-time
 * exceptions for both IOExceptions and SQLExceptions.
 * 
 * The CsvFileWriter provides two ways of writing files:
 * 
 * 1) A header and file are supplied in the constructor, followed by appending
 * the lines and finally closing the file writer.
 * 
 * 2) An SQL-Result set is provided. In this mode the CsvFileWriter will take
 * care of the extraction of the header and the complete writing of the file.
 * 
 * @author Andreas Neyer
 *
 *         Copyright 2017 afndev. All rights reserved.
 *
 */
public class CsvFileWriter {

	private FileWriter fileWriter;
	private String header;
	private long row;
	private long numCols;

	private static String LINE_SEP = "\r\n";
	private static String FIELD_SEP = ",";

	public static final Logger importLog = LoggerFactory.getLogger("import");

	/**
	 * Constructor
	 * 
	 * @param fileName:
	 *            full file name as String
	 * @param header:
	 *            full header with comma-separated fields as String
	 */
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
		numCols = header.split(FIELD_SEP).length;
	}

	/**
	 * @param fileName:
	 *            full file name as String
	 * @param header:
	 *            header as String[]
	 */
	public CsvFileWriter(String fileName, String[] header) {
		this(fileName, convertToCsv(header));
	}

	public static String convertToCsv(String[] row) {
		if (row != null) {
			String line = row[0];
			for (int i = 1; i < row.length; i++) {
				line += CsvFileWriter.FIELD_SEP;
				line += row[i];
			}
			;
			return line;
		} else {
			return null;
		}
	}

	/**
	 * @param s:
	 *            appends the line with fields already comma-separated as string
	 *            to the file
	 */
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
			importLog.error("Number of columns " + s.split(FIELD_SEP).length + " is different from header " + numCols
					+ " : Line=" + row + "Content = " + s);
		}
	}

	/**
	 * Flushes and closes the FileWriter used by CsvFileWriter
	 */
	public void close() {
		try {
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error while flushing/closing fileWriter !!!");
			e.printStackTrace();
		}
	}

	/**
	 * Internal function to write a Result Set to a file
	 * 
	 * @param rs:
	 *            query result set, header will be extracted from the result set
	 * @param fileName
	 */
	/*
	protected static void writeResultSet(ResultSet rs, String fileName) {

		String head = null;
		CsvFileWriter cfw = null;
		ResultSetMetaData metadata = null;
		int columnCount = 0;
		try {
			metadata = rs.getMetaData();
			columnCount = metadata.getColumnCount();
			head = metadata.getColumnLabel(1);
			for (int i = 2; i <= columnCount; i++) {
				head += FIELD_SEP;
				head += metadata.getColumnName(i);
			}
			System.out.println(head);

			cfw = new CsvFileWriter(fileName, head);
			while (rs.next()) {
				String row = rs.getString(1);
				for (int i = 2; i <= columnCount; i++) {
					row += FIELD_SEP + rs.getString(i);
				}
				System.out.println(row);
				cfw.appendLine(row);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (cfw != null) {
				cfw.close();
			}
		}
	} */

	/**
	 * Writes the result of a query into a comma-separated file for the use in
	 * Excel
	 * 
	 * @param dataSource
	 * @param queryString
	 * @param fileName
	 */
	// TOOD refactor fileName to File
	public static void writeQueryResult(DataSource dataSource, String queryString, String fileName) {

		System.out.println("Writing query result " + fileName);

		QueryResultTable qrt = new QueryResultTable(dataSource,queryString);
		writeQueryTable(qrt,fileName);
		
		

	}

	public static void writeQueryTable(QueryResultTable qrt, String fileName) {
		
		String head = null;
		CsvFileWriter cfw = null;
		int columnCount = 0;
			columnCount = qrt.getColumnCount();
			head = qrt.getHeaderElement(0);
			for (int i = 1; i < columnCount; i++) {
				head += FIELD_SEP;
				head += qrt.getHeaderElement(i);
			}
			System.out.println(head);

			cfw = new CsvFileWriter(fileName, head);
			
			for (int i = 0; i < qrt.getRowCount(); i++) {
				String row = qrt.get(i, 0);
				for (int j = 1; j < columnCount; j++) {
					row += FIELD_SEP + qrt.get(i, j);
				}
				System.out.println(row);
				cfw.appendLine(row);
			}
			cfw.close();	
	}
	
	public String quoteString(String s) {
		String quote = "\"";
		return quote + s + quote;
	}
}
