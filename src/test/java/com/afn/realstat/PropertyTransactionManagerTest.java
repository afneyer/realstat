package com.afn.realstat;

import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PropertyTransactionManagerTest {

	@Autowired
	PropertyTransactionManager pm;
	
	@Autowired
	PropertyTransactionRepository ptr;
	
	@Test
	public void testCleanAndLinkAll() {
		
		pm.cleanAndLinkAll();
		
	}
	
	@Test
	public void testCleanAndLinkPartialForPerformance() {
		int batchSize = 100;
		int maxBatches = 10;
		
		Function<PropertyTransaction, Boolean> cleanAndLink = pt -> {
			pm.cleanAndLink(pt);
			return true;
		};
		
		pm.performActionOnEntities(cleanAndLink, batchSize, maxBatches);
	}

}
