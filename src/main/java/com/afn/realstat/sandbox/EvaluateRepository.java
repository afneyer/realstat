package com.afn.realstat.sandbox;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afn.realstat.AgentVolume;
import com.afn.realstat.AgentVolumeRepostitory;

/**
 * @author Andreas
 *
 */
@Component
public class EvaluateRepository {
	
	/**
	 * 
	 */
 	@Autowired AgentVolumeRepostitory repository;
	// private static final Logger log = LoggerFactory.getLogger(Application.class);
 	
 	@PersistenceContext
 	EntityManager em;
 	
	public EvaluateRepository() {
	}
	
	
	public void saveOrUpdateEntity() {
		AgentVolume av = new AgentVolume("Test", new Date() );
		av.setUserCode( "testBre" );
		//String className= em.getClass().getName();
		repository.saveOrUpdate(av);
	}
}