package com.afn.realstat;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SalesFrequencyStatisticsTest {
	
	@Autowired
	SalesFrequencyStatistics sfs;

	@Test
	public void timeBetweenSales2016() {
		String fileName = "C:\\afndev\\apps\\realstat\\logs\\testoutput\\timeBetweenSales2016.txt";
		sfs.timeBetweenSales2016(fileName);
	}

}