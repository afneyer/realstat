package com.afn.realstat;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
		av.setUserCode( "testBre" );
		//String className= em.getClass().getName();
		repository.saveOrUpdate(av);
	}
}
