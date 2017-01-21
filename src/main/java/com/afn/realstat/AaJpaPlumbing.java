package com.afn.realstat;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
@Component
public class AaJpaPlumbing {
	
	/**
	 * 
	 */
 	@Autowired AgentVolumeRepostitory repository;
	// private static final Logger log = LoggerFactory.getLogger(Application.class);
 	
 	@PersistenceContext
 	EntityManager em;
 	
	public AaJpaPlumbing() {
	}
	
	
	public void saveOrUpdateEntity() {
		AgentVolume av = new AgentVolume("Test", new Date() );
		av.setBreNo( "testBre" );
		String className= em.getClass().getName();
		repository.saveOrUpdate(av);
	}
}
