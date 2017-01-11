package com.afn.realstat;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.realstat.Application;
import com.afn.realstat.CrsImporter;
import com.afn.realstat.RealPropertyRepository;

// @RunWith(SpringRunner.class)
// @SpringBootTest(classes = Application.class,
 //        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AbstractImporterTest {
	
	 // @Autowired TODO
	 // private RealPropertyRepository repository;
	 
	 // @Autowired TODO
	 // private CrsImporter importer;

	 @Test
	 public void testTranslateCsvFileDefault() {
		
	    assertEquals("apn", CrsImporter.translateCsvFileFieldDefault("APN"));
	    assertEquals("owner1",CrsImporter.translateCsvFileFieldDefault("Owner 1"));
	    assertEquals("firstName",CrsImporter.translateCsvFileFieldDefault("First Name"));
	    assertEquals("firstName2",CrsImporter.translateCsvFileFieldDefault("First Name2"));
	    assertEquals("totalSquareFootage",CrsImporter.translateCsvFileFieldDefault("Total Square Footage"));
	    assertEquals("lotSqFeet", CrsImporter.translateCsvFileFieldDefault("Lot Sq. Feet"));
	    assertEquals("listPriceSqft",CrsImporter.translateCsvFileFieldDefault("List $/Sqft"));
	    assertEquals("occPercent",CrsImporter.translateCsvFileFieldDefault("Occ%"));
	    assertEquals("pool",CrsImporter.translateCsvFileFieldDefault("Pool (Y/N)"));
	    assertEquals("priceSqft",CrsImporter.translateCsvFileFieldDefault("Price/SqFt"));
	    assertEquals("salePriceSqft",CrsImporter.translateCsvFileFieldDefault("Sale $/SqFt"));
	    assertEquals("saleLastListPrice",CrsImporter.translateCsvFileFieldDefault("Sale/Last List $"));
	    assertEquals("ticPercentOwnerOffered",CrsImporter.translateCsvFileFieldDefault("TIC % Owner Offered"));

	}
	 
}
