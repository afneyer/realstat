package com.afn.realstat;

import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.spi.PersistenceProvider;
import javax.transaction.Transactional;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@Component
public class RealPropertyManager {

	public static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	RealPropertyRepository repository;


	// define a map of properties as you would in the xml-file

	private static Properties prop = new Properties();
	static {
		prop.setProperty("provider", "org.hibernate.jpa.HibernatePersistenceProvider");
	}

	private static PersistenceProvider provider = new HibernatePersistenceProvider();
	private static EntityManagerFactory entityManagerFactory = provider.createEntityManagerFactory("default", prop);

	
	// @PersistenceContext (unitName = "afn1Persistence")
	// private EntityManagerFactory entityManagerFactory;

	// Properties p = new Properties();
	// p.put("openjpa.MetaDataFactory", "jpa(Types=FQN.class1;FQN.class2;...)");

	// @PersistenceUnit
	// EntityManagerFactory entityManagerFactory;

	@PersistenceContext
	// EntityManagerFactory factory =
	// Persistence.createEntityManagerFactory("default");
	private EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	// *
	// @PersistenceContext // (unitName = "factory", type =
	// PersistenceContextType.EXTENDED)
	// private EntityManager em;
	// *

	public RealPropertyManager() {
		em = entityManagerFactory.createEntityManager();
		log.info("Entity Manager em= " + em.toString() + " created");
	}

	private List<RealProperty> getAllEntitiesIterable(int offset, int max) {
		final TypedQuery<RealProperty> tq = em.createQuery("SELECT rp FROM RealProperty rp",
				RealProperty.class);
		tq.setFirstResult(offset);
		tq.setMaxResults(max);
		List<RealProperty> result = tq.getResultList();

		return result;
	}

	public void iterateAll() {
		int offset = 0;
		int batchSize = 100;

		List<RealProperty> listEntity;
		listEntity = getAllEntitiesIterable(offset, batchSize);
		while (listEntity.size() > 0) {
			getEntityManager().getTransaction().begin();
			for (RealProperty entity : listEntity) {
				log.info("iterating over Entity: Offset = " + offset + " EntityId =" + entity.toString());
				entity.setApnClean(entity.getApn());
				log.info("set RealProperty.apnClean to: " + entity.getApnClean() );
			}

			getEntityManager().flush();
			getEntityManager().clear();
			getEntityManager().getTransaction().commit();
			offset += batchSize;
			listEntity = getAllEntitiesIterable(offset, batchSize);
		}
	}
	
	public List<RealProperty> findByApnClean( String apn ) {
		List<RealProperty> rp = repository.findByApnClean(apn);
		return rp;
	}
}
