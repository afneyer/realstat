package com.afn.realstat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealPropertyManager {

	public static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	RealPropertyRepository repository;

	@PersistenceContext
    EntityManager em;
	
	@PersistenceUnit
	EntityManagerFactory emf;

	public RealPropertyManager() {
	}

	private List<RealProperty> getAllEntitiesIterable(int offset, int max) {
		final TypedQuery<RealProperty> tq = em.createQuery("SELECT rp FROM RealProperty rp",
				RealProperty.class);
		tq.setFirstResult(offset);
		tq.setMaxResults(max);
		List<RealProperty> result = tq.getResultList();

		return result;
	}

	@Transactional
	public void iterateAll() {
		int offset = 0;
		int batchSize = 100;

		List<RealProperty> listEntity;
		listEntity = getAllEntitiesIterable(offset, batchSize);
		while (listEntity.size() > 0) {
			for (RealProperty entity : listEntity) {
				log.info("iterating over Entity: Offset = " + offset + " EntityId =" + entity.toString());
				entity.setApnClean(entity.getApn());
				log.info("set RealProperty.apnClean to: " + entity.getApnClean() );
			}
			offset += batchSize;
			listEntity = getAllEntitiesIterable(offset, batchSize);
		}
	}
	
	public List<RealProperty> findByApnClean( String apn ) {
		List<RealProperty> rp = repository.findByApnClean(apn);
		return rp;
	}
}
