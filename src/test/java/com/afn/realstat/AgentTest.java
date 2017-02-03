	package com.afn.realstat;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
	
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AgentTest {

	@Test
	public void testToString() {
		Agent agt = new Agent("R01969778", "NEYER, ANDREAS");
		assertEquals("NEYER, ANDREAS",agt.getAgentName());
		assertEquals("Andreas", agt.getFirstName());
		assertEquals(null, agt.getMiddleName());
		assertEquals(null, agt.getMiddleInitial(), null);
		assertEquals("Neyer", agt.getLastName());
		assertEquals("1969778",agt.getLicense());
		
		agt = new Agent("00792768", "cALLAHAN, kathleen mArie");
		assertEquals( "cALLAHAN, kathleen mArie",agt.getAgentName());
		assertEquals( "Kathleen", agt.getFirstName());
		assertEquals( "Marie",agt.getMiddleName());
		assertEquals("M", agt.getMiddleInitial());
		assertEquals("Callahan",agt.getLastName());
		assertEquals("792768",agt.getLicense());
	}
}
