package com.afn.realstat;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StringTable consists of a header and table of data as strings Data in the
 * table can be accessed by index e.g. get(rowIndex, columnIndex). The first row
 * or column is 1.
 * 
 * @author Andreas Neyer
 *
 *         Copyright 2017 afndev. All rights reserved.
 *
 */
public class StringTable {
	
	public static final Logger log = LoggerFactory.getLogger("app");

	private ArrayList<String> header;
	private ArrayList<ArrayList<String>> rows;

	public StringTable(ResultSet rs) {
		ResultSetMetaData metadata;
		int columnCount;
		try {
			metadata = rs.getMetaData();
			columnCount = metadata.getColumnCount();
			header = new ArrayList<String>(columnCount);
			header.add(metadata.getColumnLabel(1));
			for (int i = 2; i <= columnCount; i++) {
				header.add(metadata.getColumnName(i));
			}

			while (rs.next()) {
				ArrayList<String> row = new ArrayList<String>(columnCount);
				row.add(rs.getString(1));
				for (int i = 2; i <= columnCount; i++) {
					row.add(rs.getString(i));
				}
				System.out.println(row);
				rows.add(row);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public String get(int i, int j) {
		return rows.get(i).get(j);
	}

	public void set(int ri, int cj, String s) {

		if (cj > header.size()) {
			String msg = "ColumnIndex for a StringTable must be smaller than the header size: Add header label(s) first!";
			msg += "columnIndex = " + cj + " header size = " + getColumnCount();
			log.error(msg);
			throw new RuntimeException(new Exception(msg));
		}

		ArrayList<String> row = null;
		if (ri <= getRowCount()) {
			row = rows.get(ri - 1);
		} else {
			row = new ArrayList<String>(getColumnCount());
			rows.add(ri - 1, row);
		}
		if (cj <= getColumnCount()) {
			row.set(cj, s);
		} else {
			row.add(cj, s);
		}

	}

	public List<String> getHeader() {
		return header;
	}
	
	public void addHeader(String headerString) {
		this.header.add(headerString);
	}

	public int getRowCount() {
		return rows.size();

	}

	public int getColumnCount() {
		return header.size();
	}
}
