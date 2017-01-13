package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DataConsistencyTests {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private RealPropertyRepository repository;

    @Test
    public void checkForDuplicateApnNumbers() {
        
    	long numRecs = repository.count();
    	log.info("RealProperty record count = " + numRecs);
    	List<RealProperty> properties = repository.findAll();

    	
    	HashMap<String, RealProperty> list = new HashMap<String, RealProperty>();
    	List<RealProperty> dupApn = new ArrayList<RealProperty>();
    	for (int i=0; i<properties.size(); i++) {
    		RealProperty prop = properties.get(i);
            String apn = prop.getApn();
    		if ( !list.containsKey(apn) ) {
    			list.put(apn, prop);	
    		} else {
    			log.info("Duplicate APN Found:");
    			dupApn.add(prop);
    		}
    		for (RealProperty dupProp : dupApn) {
    			// log.info("Duplicate APN" + dupApn );
    		}
    		assertEquals(0,dupApn.size());
    	}
    }

}
