package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)

@ActiveProfiles("prod")
public class AdReviewTourListTest {

	@Autowired
	AddressRepository adrRepo;

	@Autowired
	TourListRepository tlRepo;

	private AdReviewTourList adReviewList = null;
	private final String testDataDir = "C:\\afndev\\apps\\realstat\\testdata";

	@Before
	public void initialize() {
		File file = new File(testDataDir, "17-03-25_Tour.pdf");
		adReviewList = new AdReviewTourList(file, adrRepo, tlRepo);
		tlRepo.deleteAll();
	}

	@Test
	// TODO: further testing needed, still some wrong fields
	public void testConstructor() {
		adReviewList.createTourList();

		Date tourDate = AfnDateUtil.of(2017, 3, 27);
		List<TourListEntry> tourList = tlRepo.findByTourDate(tourDate);
		assertEquals(23, tourList.size());
		for (TourListEntry tle : tourList) {
			if (tle.getStreet() == "5728 MORAGA AVE.") {
				assertEquals("Oakland", tle.getCity());
			}
		}

		tourDate = AfnDateUtil.of(2017, 3, 28);
		tourList = tlRepo.findByTourDate(tourDate);
		assertEquals(2, tourList.size());

		tourDate = AfnDateUtil.of(2017, 3, 29);
		tourList = tlRepo.findByTourDate(tourDate);
		assertEquals(1, tourList.size());

		tourDate = AfnDateUtil.of(2017, 3, 30);
		tourList = tlRepo.findByTourDate(tourDate);
		assertEquals(15, tourList.size());

	}

	@Test
	public void testGetDates() {
		List<Date> dates = adReviewList.getDates();
		assertEquals(4, dates.size());
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

	@Test
	public void testPDFTestStripper() {

		File file = new File(testDataDir, "17-03-25_Tour.pdf");
		PDDocument doc;
		String text = null;
		try {
			doc = PDDocument.load(file);
			text = new PDFTextStripper().getText(doc);
			System.out.println(text);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void checkTourListByDate() {
		Date date = AfnDateUtil.of(2017, 3, 27);
		List<TourListEntry> tleList = tlRepo.findByTourDate(date);
		for (TourListEntry tle : tleList) {
			System.out.println(tle);
		}
	}

	@Test
	public void testFindAllDistinctDates() {
		Date date = AfnDateUtil.of(2017, 3, 27);
		List<Date> dateList = tlRepo.findAllDisctintDatesNewestFirst();
		for (Date d : dateList) {
			System.out.println(d);
		}
	}

	@Test
	public void testRemoveExtraFields() {

		/*
		 * Show that: a) extra field before the city code is removed b) second
		 * part of the city code ("HILL") is removed
		 */
		String[] example1 = { "*", "PLEAS", "HILL",
				"Stunning Modern Rancher in xxx. Dwell Magazine lvl detail & finish Park-like backyard",
				"107Roberta.com", "Patterson", "(510) 919-3333 40774448MLS#", "$1,100,000", "10-1", "❋ @ /3 2", "@",
				",", "Mc Guire R. E.Scott Leverette", "107 Roberta Ave.", "94523" };

		ArrayList<String> l1 = new ArrayList<String>(Arrays.asList(example1));
		for (String w : l1) {
			System.out.println(w);
		}
		int sizeBefore = l1.size();

		adReviewList.removeExtraFields(l1);
		for (String w : l1) {
			System.out.println(w);
		}
		assertEquals(sizeBefore - 2, l1.size());
		assertEquals("PLEAS", l1.get(0));
		assertTrue(l1.get(1).startsWith("Stunning"));

	}

	@Test
	public void importTourPdfs() {

		File dir = new File("C:\\afndev\\apps\\realstat\\logs\\uploads");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File file : directoryListing) {
				String fileName = file.getName();
				System.out.println("Importing file = " + fileName);
				AdReviewTourList atl = new AdReviewTourList(file, adrRepo, tlRepo);
				atl.createTourList();
			}
		} else {
			fail();
		}
	}

	@Test
	public void verifyParser01() {

		File file = new File(testDataDir, "17-04-27_Update-.pdf");
		AdReviewTourList atl = new AdReviewTourList(file, adrRepo, tlRepo);
		atl.createTourList();

		// write out the text file for debugging
		writeText(atl.getText(), file);
	}

	public void writeText(String text, File file) {
		String fileName = FilenameUtils.getBaseName(file.getName());
		fileName += ".txt";
		File txtFile = new File(testDataDir, fileName);
		PrintWriter printWriter;
		try {
			printWriter = new PrintWriter(txtFile);
			printWriter.print(text);
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
