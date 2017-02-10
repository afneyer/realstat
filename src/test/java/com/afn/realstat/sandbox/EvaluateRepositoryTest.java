package com.afn.realstat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AaJpaPlumbingTest {
	 
	 @Autowired
	 private AaJpaPlumbing testSample;

	@Test
	public void testJpaSetup() {
		testSample.saveOrUpdateEntity();
	}
}
