package com.afn.realstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseDouble2;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseInt2;
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
		entityClass = AgentVolume.class;
	}
	

	/**
	 * @return
	 */
	protected CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {

				new Optional(new ParseDate("YYYY")), // Year
				new Optional(), // Agent Raw
				new Optional(), // Office Raw	
				new Optional(new ParseInt2()), // Units Listed
				new Optional(new ParseDouble2()), // Volume Listed
				new Optional(new ParseInt2()), // Units Sold
				new Optional(new ParseDouble2()), // Volume Sold
				new Optional(new ParseInt2()), // Units Total
				new Optional(new ParseDouble2()), // Volume Total
				new Optional(new ParseDouble2()), // % MLS Volume
				new Optional(new ParseDouble2()), // Avg Ttl Price
				new Optional(new ParseDouble2()), // Avg DOM
				
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
		AgentVolume av = (AgentVolume)e;
		av.setBreNo( av.extractBrefromAgentRaw() );
		repository.saveOrUpdate(av);
	}
}