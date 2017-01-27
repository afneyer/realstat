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
public class AgentVolumeTest {

	@Test
	public void testExtractBreFromAgentRaw() {
		SimpleDateFormat year = new SimpleDateFormat("yyyy");
		AgentVolume agv1 = null;
		try {
			agv1 = new AgentVolume("206", year.parse("2016"));
		} catch (ParseException e) {
			fail("Cannot parse date");
		}

		agv1.setAgentRaw("Lesterhuis, Hendrik - 159521452");
		assertEquals("159521452", agv1.extractBrefromAgentRaw());
		agv1.setAgentRaw("MCRAE, BEBE - R00875159");
		assertEquals("00875159", agv1.extractBrefromAgentRaw());
	}
}
