package com.afn.realstat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.realstat.Application;
import com.afn.realstat.CustomerRepository;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DataConsistencyTests {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private RealpropertyRepository repository;

    @Test
    public void checkForDuplicateApnNumbers() {
        
    	long numRecs = repository.count();
    	log.info("Realproperty record count = " + numRecs);
    	List<Realproperty> properties = repository.findAll();

    	
    	HashMap<String, Realproperty> list = new HashMap<String, Realproperty>();
    	List<Realproperty> dupApn = new ArrayList<Realproperty>();
    	for (int i=0; i<properties.size(); i++) {
    		Realproperty prop = properties.get(i);
            String apn = prop.getApn();
    		if ( !list.containsKey(apn) ) {
    			list.put(apn, prop);	
    		} else {
    			log.info("Duplicate APN Found:");
    			dupApn.add(prop);
    		}
    		for (Realproperty dupProp : dupApn) {
    			// log.info("Duplicate APN" + dupApn );
    		}
    		assertEquals(0,dupApn.size());
    	}
    }

    @Test
    public void shouldFindTwoBauerCustomers() {
        then(this.repository.findByLastNameStartsWithIgnoreCase("Bauer")).hasSize(2);
    }
}
