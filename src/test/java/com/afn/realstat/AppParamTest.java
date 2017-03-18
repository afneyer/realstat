	package com.afn.realstat;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.assertj.core.util.DateUtil;
import org.hibernate.event.spi.AbstractPreDatabaseOperationEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
	
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AppParamTest {
	
	@Autowired
	AppParamRepository apRepo;
	
	@Autowired 
	AppParamManager apMgr;
	
	@Before
	public void setUp() {
		apRepo.deleteAll();
		apMgr.initializeParameters();
	}
	
	

	@Test
	public void testParameterInitialization() {
		assertEquals("50", apMgr.get("maxCallsPerSecond", "MAP").getParamValue());
		assertEquals("2000", apMgr.get("maxCallsPerDay", "MAP").getParamValue());
		assertEquals("0", apMgr.get("callsToday","MAP").getParamValue());
	    assertEquals("2017-03-01 10:00:00.000", apMgr.getVal("lastCall","MAP"));	
	    
	    long paramCount1 = apRepo.count();
	    
	    // initialize a second time and count
	    apMgr.initializeParameters();
	    long paramCount2 = apRepo.count();
	    assertEquals(paramCount1, paramCount2);
	}
	
	@Test
	public void testChangingParameter() {
		Date d1 = new Date();
		String str1 = DateUtil.formatAsDatetimeWithMs(d1);
		apMgr.setVal("lastCall", "MAP", d1);
		Date d2 = apMgr.getDateVal("lastCall", "MAP");
		String str2 = DateUtil.formatAsDatetimeWithMs(d2);
		assertEquals(str1,str2);
		assertTrue(d1.equals(d2));
	}
	
}
