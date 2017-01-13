package com.afn.realstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.vaadin.spring.annotation.SpringComponent;

/**
 * @author Andreas
 *
 */
@SpringComponent
public class AgentVolumeImporter extends AbstractImporter {
	
	/**
	 * 
	 */
 	@Autowired AgentVolumeRepostitory repository;
	// private static final Logger log = LoggerFactory.getLogger(Application.class);

	public AgentVolumeImporter() {
		entityClass = RealProperty.class;
	}
	

	/**
	 * @return
	 */
	protected CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {

				new Optional(), // Agent Raw
				new Optional(), // Office Raw
				new Optional(), // BreNo
				new Optional(new ParseDate("YYYY")), // Year
				new Optional(new ParseInt()), // Units Listed
				new Optional(new ParseDouble()), // Volume Listed
				new Optional(new ParseInt()), // Units Sold
				new Optional(new ParseDouble()), // Volume Sold
				new Optional(new ParseInt()), // Units Total
				new Optional(new ParseDouble()), // Volume Total
				new Optional(new ParseDouble()), // % MLS Volume
				new Optional(new ParseDouble()), // Avg Ttl Price
				new Optional(new ParseDouble()), // Avg DOM
				
		};

		return processors;
	}

	/**
	 * Maps the header of the csv-file (i.e. list of fields) into the field names of the entity
	 * The default translator can be used if the entity are similar to the fields in the csv-file
	 * 
	 * @param header
	 * @return
	 */
	protected String[] mapHeader(String[] header) {

		// translate each element of the header to the corresponding bean field
		// name
		for (int i = 0; i < header.length; i++) {
			
			// special translations
			switch (header[i]) {
			case "Owner 1":
				header[i] = "owner1";
				break;
			default:
				header[i] = translateCsvFileFieldDefault(header[i]);
			}
		}
		return header;
	}

	protected void saveOrUpdateEntity(AbstractEntity e) {
		AgentVolume rp = (AgentVolume)e;
		repository.saveOrUpdate(rp);
	}
}
