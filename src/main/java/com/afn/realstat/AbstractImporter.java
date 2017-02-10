package com.afn.realstat;

import java.beans.Statement;
import java.io.File;
import java.io.FileReader;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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
public abstract class AbstractImporter<T extends AbstractEntity> {

	// @Autowired RealPropertyRepository repository;

	private static final Logger importLog = LoggerFactory.getLogger("import");
	protected CsvPreference csvPref = CsvPreference.STANDARD_PREFERENCE;
	
	@SuppressWarnings("rawtypes")
	Class entityClass;

	public AbstractImporter() {
	}
	
	protected abstract void preProcessEntity(T entity);
	protected abstract void saveOrUpdateEntity(T entity);
	protected abstract void postProcessEntity(T entity);

	public void importFile(File importFile) {

		try {
			readWithCsvBeanReader(importFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// print all records
		// CustomerRepository repository;
		// log.info( new Long(repository.count()).toString() );
		// log.info("After all records have been read");

	}

	/**
	 * @param file
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void readWithCsvBeanReader(File file) throws Exception {

		ICsvBeanReader beanReader = null;
		try {

			beanReader = new CsvBeanReader(new FileReader(file), csvPref);

			// the header elements are used to map the values to the bean (names
			// must match)
			String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();

			header = mapHeader(header);

			T entity;
			while ((entity = (T) beanReader.read(entityClass, header, processors)) != null) {
				importLog.info("Importer before saving or updating entity:");
				importLog.info("   Csv Line Nbr  =" + beanReader.getLineNumber());
				importLog.info("   Csv Row Nbr   =" + beanReader.getRowNumber());
				importLog.info("   Entity Class  =" + entityClass);
				importLog.info("   Entity String =" + entity);
				preProcessEntity(entity);
				if (entity.isValid()) {
					saveOrUpdateEntity(entity);
					postProcessEntity(entity);
				} else {
					importLog.warn("Invalid entity skipped: Class=" + entity.getClass() + "  Entity=" + entity.toString());
				}
			}

		}

		finally

		{
			if (beanReader != null) {
				beanReader.close();
			}
		}

	}

	

	// abstract void readAndSaveNextEntity( CsvBeanReader r );

	/**
	 * @return
	 */
	protected abstract CellProcessor[] getProcessors();

	/**
	 * @param header
	 * @return
	 */
	protected abstract String[] mapHeader(String[] header);

	/**
	 * @param inStr
	 *            Field to be translated from a CSV file header field into a
	 *            entity field If the default translator does not work the field
	 *            can also be mapped individually;
	 * @return
	 */
	static String translateCsvFileFieldDefault(String inStr) {

		String str = inStr;
		String retStr = new String();

		str = str.replaceAll("\\(Y\\/N\\)", " ");
		str = str.replaceAll("\\%", " Percent ");
		str = str.replaceAll("\\$", " Price ");
		str = str.replaceAll("\\/", " ");
		str = str.replaceAll("\\.", " ");
		str = str.replaceAll("\\-", " ");
		str = str.replaceAll("  ", " ");

		// make it all lower case
		str = str.toLowerCase();

		// change all characters after blank to upper case and remove blank
		char previous = '#';
		for (int i = 0; i < str.length(); i++) {

			char c = str.charAt(i);
			if (c != ' ') {
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
	 * Utility method to set a field of an entity based on a field value
	 * (probably already exists somewhere)
	 * 
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
