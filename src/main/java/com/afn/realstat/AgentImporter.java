package com.afn.realstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.vaadin.spring.annotation.SpringComponent;

/**
 * @author Andreas
 *
 */
@SpringComponent
public class AgentImporter extends AbstractImporter {
	
	/**
	 * 
	 */
 	@Autowired AgentRepository agentRep;
	// private static final Logger log = LoggerFactory.getLogger(Application.class);

	public AgentImporter() {
		entityClass = Agent.class;
	}
	

	/**
	 * @return
	 */
	protected CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {
				
				new NotNull(), // Agent Name
				new NotNull(), // User Code
				new Optional(), // Office Name
				new Optional(), // Office Code
				new Optional(), // Office Phone
				new Optional(), // Type
				new Optional(), // Status
				new Optional(), // License
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
			case "xxxxxxxxSamplexxxxx":
				break;
			default:
				header[i] = translateCsvFileFieldDefault(header[i]);
			}
		}
		return header;
	}

	protected void saveOrUpdateEntity(AbstractEntity e) {
		Agent a = (Agent)e;
		agentRep.saveOrUpdate(a);
	}
}
