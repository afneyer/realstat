package com.afn.realstat;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PropertyTransactionServiceTest {
	
	@Autowired
	private PropertyTransactionService srvc;

	@Test
	public void getPropertyLocationsForAgent() {
		String license = "792768";
		srvc.getPropertyLocationsForAgent(license);
	}

}
