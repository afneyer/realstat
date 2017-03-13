package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AddressManagerTest {

	@Autowired
	AddressManager adrMgr;

	@Test
	public void testCleanAndFixAddresses() {
		adrMgr.cleanAndFixAddresses();
	}
	
}
