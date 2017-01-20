package com.afn.realstat;

import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.persistence.spi.PersistenceProvider;
import javax.transaction.Transactional;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.vaadin.spring.annotation.SpringComponent;

@Repository
@Transactional
@Component
public class RealPropertyTransactionManager {
	
	public static final Logger log = LoggerFactory.getLogger(Application.class);
	
	private static final String PERSISTENT_UNIT_NAME = "default";
 
	// define a map of properties as you would in the xml-file

	private static Properties prop = new Properties(); 
	static {
		prop.setProperty("provider", "org.hibernate.jpa.HibernatePersistenceProvider");
	}

	private static PersistenceProvider provider = new HibernatePersistenceProvider();
	private static EntityManagerFactory entityManagerFactory =  
           provider.createEntityManagerFactory("default",prop);
	
	@Autowired
	private PropertyTransactionRepository repository;
	
	// @PersistenceContext (unitName = "afn1Persistence")
	// private EntityManagerFactory entityManagerFactory;
	
	// Properties p = new Properties();
	// p.put("openjpa.MetaDataFactory", "jpa(Types=FQN.class1;FQN.class2;...)");
	
	// @PersistenceUnit
	// EntityManagerFactory entityManagerFactory;
	
	@PersistenceContext
    // EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
    private EntityManager em;
	
	public EntityManager getEntityManager() {
		return em;
	}
	
	//*
	// @PersistenceContext // (unitName = "factory", type = PersistenceContextType.EXTENDED)
	// private EntityManager em;
	//*
    
	public RealPropertyTransactionManager() {
		em = entityManagerFactory.createEntityManager();
		log.info("Entity Manager em= " + em.toString() + " created");
	}

	private List<PropertyTransaction> getAllPropertyTransactionsIterable(int offset, int max)
	{
		final TypedQuery<PropertyTransaction> tq = em.createQuery("SELECT pt FROM PropertyTransaction pt", PropertyTransaction.class);
		tq.setFirstResult(offset);
		tq.setMaxResults(max);
		List<PropertyTransaction> result = tq.getResultList();
		
	    return result;
	}
	
	public void iterateAll()
	{
	    int offset = 0;
	    int batchSize = 100;

	    List<PropertyTransaction> listPt;
	    listPt = getAllPropertyTransactionsIterable(offset, batchSize);
	    while (listPt.size() > 0)
	    {
	        getEntityManager().getTransaction().begin();
	        for (PropertyTransaction pt : listPt)
	        {
	            log.info("iterating over pt: Offset = " + offset + " PT=" + pt.toString());
	        }

	        getEntityManager().flush();
	        getEntityManager().clear();
	        getEntityManager().getTransaction().commit();
	        offset += batchSize;
	        listPt = getAllPropertyTransactionsIterable(offset, batchSize);
	    }
	}
}
