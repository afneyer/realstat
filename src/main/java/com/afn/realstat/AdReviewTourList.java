package com.afn.realstat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdReviewTourList {

	public static final Logger log = LoggerFactory.getLogger("app");

	private Map<Date, List<Address>> tours = new HashMap<Date, List<Address>>();
	private AddressRepository adrRepo;
	private TourListRepository tlRepo;
	private String text;

	public AdReviewTourList() {
	};

	public AdReviewTourList(File file, AddressRepository adrRepo, TourListRepository tlRepo) {
		this.adrRepo = adrRepo;
		this.tlRepo = tlRepo;
		if (file == null) {
			file = new File("C:\\afndev\\apps\\realstat\\testdata", "17-03-25_Tour.pdf");
		}
		text = getText(file);
		createTourList(text);
	}

	public List<Address> getAdresses(Date date) {
		List<Address> adrList = tours.get(date);
		return adrList;
	}

	public List<Date> getDates() {
		List<Date> dates = new ArrayList<Date>(tours.keySet());
		Collections.sort(dates);
		return dates;

	}

	@Transactional
	public void createTourList(String text) {

		// System.out.println("Text in PDF: " + text);
		// String[] lines = text.split(System.lineSeparator());
		tours = new HashMap<Date, List<Address>>();

		String[] articles = text.split("\\^");

		for (String article : articles) {
			String[] lines = article.split("\n");

			Date tourDate = null;
			int i = 0;

			while (i < lines.length) {
				int processedLines = 0;
				String line = lines[i];

				String[] words1 = line.split("\\|");

				Date tmpTourDate = getTourDate(words1);
				if (tmpTourDate != null) {
					if (!tmpTourDate.equals(tourDate)) {
						tourDate = tmpTourDate;
						System.out.println("- dat: " + tourDate);
						tours.put(tourDate, new ArrayList<Address>());
					} else {
						System.out.println("- dat: " + tourDate + " found same tour date again");
					}
				}

				String city = null;
				int j = 0;
				while (j < words1.length) {
					city = getCity(words1[j]);
					if (city != null)
						break;
					j++;
				}

				if (city != null) {
					int numLines = 2;
					String[] words2 = lines[i + 1].split("\\|");

					String street = words1[j + 1];
					String zip = words2[0];

					Address adr = new Address(street, city, zip);
					log.info(adr.toString());

					log.info(street + ", " + city + ", " + zip);

					
					 if (adr != null) {
					  
						adrRepo.saveOrUpdate(adr);
						List<Address> adrList = tours.get(tourDate);
						adrList.add(adr);

						TourListEntry tourListEntry = new TourListEntry(tourDate, adr);
						// setTourListEntryFields(tourListEntry,
						// tourListEntryLines);
						tlRepo.saveOrUpdate(tourListEntry);
					 }
					 
					processedLines = numLines;
				}

				if (processedLines == 0) {
					processedLines = 1;
				}

				i += processedLines;
			}
		}

	}

	private void setTourListEntryFields(TourListEntry tourListEntry, String[] adrArray) {
		int numFields = adrArray.length;
		tourListEntry.setDescription(adrArray[1]);
		tourListEntry.setPrice(adrArray[numFields - 8]);
		// tourListEntry.setAgent(agent);
		// tourListEntry.setBedBath(bedBath);

	}

	private Address getAddress(String[] lines) {

		String city = null;
		String zip = null;
		String fullStreet = null;

		int streetIndex = lines.length - 2;
		if (streetIndex < 0) {
			System.out.println("Invalid street Index : " + Arrays.deepToString(lines));
		} else {
			fullStreet = lines[streetIndex];
		}

		// System.out.println("--- city: " + city);
		int zipIndex = lines.length - 1;
		if (zipIndex < 0) {
			System.out.println("Invalid zip Index : " + Arrays.deepToString(lines));
		} else {
			zip = lines[zipIndex];
			if (zip == null) {
				System.out.println("Invalid zip : " + Arrays.deepToString(lines));
			} else {
				zip = zip.trim();
			}
		}
		city = getCity(lines[0]);

		// create address
		Address adr = new Address(fullStreet, city, zip);
		System.out.println(adr);

		return adr;
	}

	private String getCity(String line) {
		String city = line.trim();
		switch (city) {
		case "PIED":
			return "Piedmont";
		case "OAK":
			return "Oakland";
		case "PLEAS":
			return "Oakland";
		case "ALA":
			return "Oakland";
		case "HAY":
			return "Hayward";
		case "KENS":
			return "Kensington";
		case "BERK":
			return "Berkeley";
		case "RICH":
			return "Richmond";
		default:
			return null;
		}
	}

	private Date getTourDate(String[] words) {
		Date date = null;
		// TODO iterate through words of the line
		for (String word : words) {

			SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd, yyyy");
			try {
				date = formatter.parse(word);
				return date;
			} catch (ParseException e) {
			}
		}
		return date;
	}

	private String getText(File pdfFile) {

		try {
			PDDocument doc = PDDocument.load(pdfFile);
			PDFTextStripper pdfStrip = new PDFTextStripper();
			pdfStrip.setArticleStart("");
			pdfStrip.setArticleEnd("^");
			pdfStrip.setParagraphStart("");
			pdfStrip.setParagraphEnd("\n");
			pdfStrip.setWordSeparator("|");
			pdfStrip.setLineSeparator("|");
			pdfStrip.setSortByPosition(true);
			/*
			 * System.out.println("|" + pdfStrip.getArticleStart() + "|");
			 * System.out.println("|" + pdfStrip.getArticleEnd() + "|");
			 * System.out.println("|" + pdfStrip.getParagraphStart() + "|");
			 * System.out.println("|" + pdfStrip.getParagraphEnd() + "|");
			 * System.out.println("|" + pdfStrip.getWordSeparator() + "|");
			 */

			text = pdfStrip.getText(doc);

			System.out.println("|" + pdfStrip.getArticleStart() + "|");
			System.out.println("|" + pdfStrip.getArticleEnd() + "|");
			System.out.println("|" + pdfStrip.getParagraphStart() + "|");
			System.out.println("|" + pdfStrip.getParagraphEnd() + "|");
			System.out.println("|" + pdfStrip.getWordSeparator() + "|");

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return text;
	}
}
