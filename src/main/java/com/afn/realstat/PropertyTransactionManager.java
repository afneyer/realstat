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
	    	
	        //tm.getTransaction(null); TODO
	    	// DefaultTransactionDefinition dtd = new DefaultTransactionDefinition();
	    	// dtd.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
	    	// TransactionStatus ts = ptm.getTransaction(dtd);
	    	performEntityAction(listPt);
	    	
	    	// Print this out
//	    	int i = 0; TODO
//	    	for  (PropertyTransaction pt : listPt) {
//	    		String state = pt.getState();
//	    		importLog.info("Offset = " + offset + " listIndex = " + i + " state = " + state + " pt = " + pt.toString() );
//	    		i++;
//	    	}
//	    	
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
		
		int i = 0;
		for (PropertyTransaction pt : entityList)
        {
			
			// DebugUtils.transactionRequired("PropertyTransactionManager.performEntityAction");
            importLog.info("index =" + i + " iterating over PT=" + pt.toString());
            linkPropertyTransactionToRealProperty( pt );
            // repository.save(pt)
            i++;     
        }
		
	}
	
	@Transactional
	public void linkPropertyTransactionToRealProperty( PropertyTransaction pt ) {
		
		RealProperty rp = null;
		String apnClean = RealStatUtil.cleanApn(pt.getApn());
		List<RealProperty> list = rpRepo.findByApnClean(apnClean);
			
		if ( list.size() == 1 ) {
			rp = list.get(0);
			pt.setRealProperty(rp);
			importLog.info("Property Transaction Linked By APN= " + pt.toString());
			return;
		}
		
		if (list.size() == 0) {
			// no corresponding property found
			importLog.warn("No property found by APN=" + pt.getApn() + " apnClean =" + apnClean + " for propertyTransaction = " + pt.toString() );
			this.linkPropertyTransactionToRealPropertyByAddress(pt);
			return;
		}

		for (RealProperty rp1 : list) {
		   importLog.warn("Multiple properties by APN! pt = " + pt.toString() + "rp = " + rp1.toString() );
		   this.linkPropertyTransactionToRealPropertyByAddress(pt);
		   return;
		}
		
	}
	
	@Transactional
public void linkPropertyTransactionToRealPropertyByAddress( PropertyTransaction pt ) {
		
		RealProperty rp = null;
		Address adrClean = new Address( pt.getAddress(), pt.getUnit(), pt.getCity(), pt.getZip());
		String adrCleanStr = adrClean.getCleanAddress();
		List<RealProperty> list = rpRepo.findByAddressClean(adrCleanStr);
		
		if ( list.size() == 1 ) {
			rp = list.get(0);
			pt.setRealProperty(rp);
			importLog.warn("Property Transaction Linked By Address= " + pt.toString());
			return;
		}
		
		if (list.size() == 0) {
			// no corresponding property found
			importLog.warn("No property found by Adress");
			importLog.warn("raw Address=" + adrClean.getRawAddress());
			importLog.warn("clean Address" + adrClean.getCleanAddress());
			importLog.warn("prop trans=" + pt.toString());
			return;
		}
	
		for (RealProperty rp1 : list) {
		   importLog.warn("Multiple properties by Adress! pt = " + pt.toString() + "rp = " + rp1.toString() );
		   return;
		}
		
	}
	
}
