package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)

@ActiveProfiles("prod")
public class AdReviewTourListTest {
	
	@Autowired
	AddressRepository adrRepo;
	 
	private AdReviewTourList adReviewList = null;
	
	@Before
	public void testConstructor () {
		adReviewList = new AdReviewTourList(null, adrRepo);
	}
	
	@Test
	public void testGetDates() {
		List<Date> dates = adReviewList.getDates();
		assertEquals(4,dates.size());
		for (Date d : dates) {
			System.out.println(d);
		}
	}
	
	@Test
	public void testGetAddressesForDate() {
		List<Date> dates = adReviewList.getDates();
		List<Address> adr = null;
		adr = adReviewList.getAdresses(dates.get(0));
		assertEquals(23, adr.size());
		adr = adReviewList.getAdresses(dates.get(1));
		assertEquals(2, adr.size());
		adr = adReviewList.getAdresses(dates.get(2));
		assertEquals(1, adr.size());
		adr = adReviewList.getAdresses(dates.get(3));
		assertEquals(15, adr.size());
	}

}
