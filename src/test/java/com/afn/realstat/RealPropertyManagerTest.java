package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RealPropertyManagerTest {

	@Autowired
	RealPropertyManager rpmgr;
	
	@Autowired
	PropertyTransactionManager ptmgr;
	
	@Autowired
	PropertyTransactionRepository ptRepo;
	
	@Test
	public void testPropertyTransactionLinking() {
		String apnClean = "1311287";
		
		// find RealProperty
		List<RealProperty> rpList = rpmgr.findByApnClean(apnClean);
		RealProperty rp = null;
		if ( rpList.size() == 1 ) {
			rp = rpList.get(0);
		} 
		if (rp == null) {
			fail("no RealProperty found for apnClean=" + apnClean);
		}
		
		
		// find PropertyTransaction
		List<PropertyTransaction> ptList = ptRepo.findByApnClean(apnClean);
		PropertyTransaction pt = null;
		if (ptList.size() == 1 ) {
			pt = ptList.get(0);
		}
		if (pt == null) {
			fail("no PropertyTransaction found for apnClean=" + apnClean);
		}
		
		assertEquals( pt.getApnClean(), rp.getApnClean());
		
	}
	
	
	@Test
	public void testIteration() {		
		rpmgr.iterateAll();
	}

}
