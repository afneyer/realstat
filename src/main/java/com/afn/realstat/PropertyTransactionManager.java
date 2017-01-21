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
public class PropertyTransactionManager {
	
	public static final Logger log = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	PropertyTransactionRepository repository;
	
	@Autowired
	RealPropertyRepository rpRepo;
	
	@PersistenceContext
    EntityManager em;
	
	@PersistenceUnit
	EntityManagerFactory emf;
	
    
	public PropertyTransactionManager() {
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
	        //tm.getTransaction(null);
	        performEntityAction(listPt);

	        //em.flush();
	        //em.clear();
	        //em.getTransaction().commit();
	        offset += batchSize;
	        listPt = getAllPropertyTransactionsIterable(offset, batchSize);
	    }
	}
	
	@Transactional
	public void performEntityAction( List< PropertyTransaction> entityList ) {
		for (PropertyTransaction pt : entityList)
        {
            log.info("iterating over PT=" + pt.toString());
            RealProperty rp = linkPropertyTransactionToRealProperty( pt );
        }
		
	}
	
	public RealProperty linkPropertyTransactionToRealProperty( PropertyTransaction pt ) {
		
		RealProperty property = null;
		List<RealProperty> list = rpRepo.findByApnClean(pt.getApnClean());
		
		if (list.size() == 0) {
			// no corresponding property found
			log.info("No property found by APN for propertyTransaction = " + this.toString() );
			return property;
		}
	
		if ( list.size() == 1 ) {
			property = list.get(0);
			pt.setProperty(property);
			log.info("Property Transaction Linked = " + this.toString());
			return property;
		}
		
		for (RealProperty rp : list) {
		   log.info("Need address comparison! pt = " + this.toString() + "rp = " + rp.toString() );
		   return property;
		}
		
		log.info("No property found for Address propertyTransaction = " + this.toString() );
		return null;	
	}
}
