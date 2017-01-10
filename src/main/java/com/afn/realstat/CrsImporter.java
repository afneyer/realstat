package com.afn.realstat;

import java.beans.Statement;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseDouble2;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.vaadin.spring.annotation.SpringComponent;

/**
 * @author Andreas
 *
 */
@SpringComponent
public class CrsImporter {
	
	/**
	 * 
	 */
	@Autowired RealpropertyRepository repository;
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	/**
	 * 
	 */
	public CrsImporter() {
	}
	
	/**
	 * @param crsFile
	 */
	public void importFile(File crsFile) {

		try {
			readWithCsvBeanReader(crsFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// print all records
		// CustomerRepository repository;
		log.info( new Long(repository.count()).toString() );
		log.info("After all records have been read");
		
		
	}

	/**
	 * @param file
	 * @throws Exception
	 */
	private void readWithCsvBeanReader( File file ) throws Exception {

		ICsvBeanReader beanReader = null;
		try {

			beanReader = new CsvBeanReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);

			// the header elements are used to map the values to the bean (names
			// must match)
			String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();

			header = mapHeader(header);

			Realproperty property;
			while ((property = beanReader.read(Realproperty.class, header, processors)) != null) {
				repository.save(property);
				System.out.println(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(),
						beanReader.getRowNumber(), property));
			}

		}

		finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}

	}

	/**
	 * @return
	 */
	private static CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {

				new Optional(), // Owner 1
				new Optional(), // Title
				new Optional(), // First Name
				new Optional(), // Middle
				new Optional(), // Last Name
				new Optional(), // Owner 2
				new Optional(), // Title2
				new Optional(), // First Name2
				new Optional(), // Middle2
				new Optional(), // Last Name2
				new Optional(), // Owner Address
				new Optional(), // Owner City
				new Optional(), // Owner State
				new Optional(), // Owner Zip
				new Optional(), // APN
				new Optional(), // Improvement Type
				new Optional(), // Land Use
				new NotNull(), // Property Address
				new NotNull(), // Property City
				new NotNull(), // Property State
				new NotNull(), // Property Zip
				new Optional(new ParseDouble()), // Total Square Footage
				new Optional(new ParseDate("MM/dd/yyyy")), // Last Sale Date
				new Optional(new ParseDouble()), // Last Sale Amount
				new Optional(), // Warranty Deed Book
				new Optional(), // Warranty Deed Page
				new Optional(), // Warranty Deed Document Number
				new Optional(new ParseDouble()), // Land Value
				new Optional(new ParseDouble()), // Improvement Value
				new Optional(new ParseDouble()), // Total Value
				new Optional(new ParseDate("yyyy")), // Year Built
				new Optional(), // Dimensions
				new Optional(), // Subdivision
				new Optional( new ParseDouble() ), // Acreage
				new Optional(), // Block
				new Optional(), // Lot
				new Optional( new ParseDouble2() ), // Lot Sq. Feet
				new Optional(), // Lot Dimensions
				new Optional(), // Census Tract
		};

		return processors;
	}

	/**
	 * @param header
	 * @return
	 */
	private static String[] mapHeader(String[] header) {

		// translate each element of the header to the corresponding bean field
		// name
		for (int i = 0; i < header.length; i++) {
			// special translations
			switch (header[i]) {
			case "Owner 1":
				header[i] = "owner1";
				break;
			default:
				header[i] = translateCrsFieldDefault(header[i]);
			}
		}
		return header;
	}

	/**
	 * @param str
	 * @return
	 */
	/**
	 * @param str
	 * @return
	 */
	static String translateCrsFieldDefault(String str) {

		String retStr = new String();
		
		// make it all lower case
		str = str.toLowerCase();

		// change all characters after blank to upper case and remove blank
		char previous = '#';
		for (int i = 0; i < str.length(); i++) {
			
			char c = str.charAt(i);
			if (c != ' ' && c!= '.') {
				if (previous == ' ') {
					retStr += Character.toUpperCase(c);
				} else {
				   retStr += c;
				}
			}
			
			previous = c;

		}

		return retStr;
	}

	/**
	 * @param bean
	 * @param field
	 * @param value
	 * @throws Exception
	 */
	public void setFieldByString(Object bean, String field, String value) throws Exception {
		Statement stmt;
		stmt = new Statement(bean, field, new Object[] { value });
		stmt.execute();
	}

}
