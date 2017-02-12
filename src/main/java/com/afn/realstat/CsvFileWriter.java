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
import org.springframework.jdbc.datasource.DataSourceUtils;

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

	public void close() {
		try {
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error while flushing/closing fileWriter !!!");
			e.printStackTrace();
		}
	}
	
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
	}
	
	public static void writeQueryResult(DataSource dataSource, String queryString, String fileName) {
		
		System.out.println("Writing query result " + fileName);
		
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(queryString);

			ResultSet rs = ps.executeQuery();
			CsvFileWriter.writeResultSet(rs, fileName);
			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}

		}
		
	}
}
