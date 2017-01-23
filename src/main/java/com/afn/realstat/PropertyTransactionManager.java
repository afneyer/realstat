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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class PropertyTransactionManager {
	
	public static final Logger importLog = LoggerFactory.getLogger("import");
	
	@Autowired
	PropertyTransactionRepository repository;
	
	@Autowired
	RealPropertyRepository rpRepo;
	
	@PersistenceContext
    EntityManager em;
	
	@PersistenceUnit
	EntityManagerFactory emf;
	
	@Autowired
	PlatformTransactionManager ptm;
	
    
	public PropertyTransactionManager() {
	}

	public List<PropertyTransaction> getAllPropertyTransactionsIterable(int offset, int max)
	{
		final TypedQuery<PropertyTransaction> tq = em.createQuery("SELECT pt FROM PropertyTransaction pt", PropertyTransaction.class);
		tq.setFirstResult(offset);
		tq.setMaxResults(max);
		List<PropertyTransaction> result = tq.getResultList();
		
	    return result;
	}
	
	// @Transactional
	public void iterateAll()
	{
	    int offset = 0;
	    int batchSize = 100;

	    List<PropertyTransaction> listPt;
	    
	    listPt = getAllPropertyTransactionsIterable(offset, batchSize);
	    while (listPt.size() > 0)
	    {
	    	
	        //tm.getTransaction(null);
	    	// DefaultTransactionDefinition dtd = new DefaultTransactionDefinition();
	    	// dtd.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
	    	// TransactionStatus ts = ptm.getTransaction(dtd);
	    	performEntityAction(listPt);
	    	repository.save(listPt);
            // ts.flush();
	        // ptm.commit(ts);

	        //em.flush();
	        //em.clear();
	        //em.getTransaction().commit();
	        offset += batchSize;
	        listPt = getAllPropertyTransactionsIterable(offset, batchSize);
	    }
	}
	
	public void performEntityAction( List< PropertyTransaction> entityList ) {
		for (PropertyTransaction pt : entityList)
        {
			
			// DebugUtils.transactionRequired("PropertyTransactionManager.performEntityAction");
            importLog.info("iterating over PT=" + pt.toString());
            linkPropertyTransactionToRealProperty( pt );
            // TODO remove the following line
            pt.setState("CA");
            
        }
		
	}
	
	@Transactional
	public void linkPropertyTransactionToRealProperty( PropertyTransaction pt ) {
		
		RealProperty rp = null;
		List<RealProperty> list = rpRepo.findByApnClean(pt.getApnClean());
		
		if (list.size() == 0) {
			// no corresponding property found
			importLog.info("No property found by APN for propertyTransaction = " + pt.toString() );
		}
	
		if ( list.size() == 1 ) {
			rp = list.get(0);
			pt.setRealProperty(rp);
			importLog.info("Property Transaction Linked = " + pt.toString());
		}
		
		for (RealProperty rp1 : list) {
		   importLog.info("Need address comparison! pt = " + pt.toString() + "rp = " + rp1.toString() );
		}
		
		importLog.info("No property found for Address propertyTransaction = " + pt.toString() );
	}
}
