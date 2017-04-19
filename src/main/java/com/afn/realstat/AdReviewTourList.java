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

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.helper.StringUtil;
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
	private File file;

	public AdReviewTourList() {
	};

	public AdReviewTourList(File file, AddressRepository adrRepo, TourListRepository tlRepo) {
		this.adrRepo = adrRepo;
		this.tlRepo = tlRepo;
		this.file = file;
	}
	
	public void createTourList() {
		String text = getText(file);
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

				List<String> words1 = new ArrayList<String>(Arrays.asList(line.split("\\|")));

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

				removeExtraFields(words1);
				String city = getCity(words1.get(0));
				if (city != null) {
					// TODO find the next city code because the number of lines per entry is not consistently 2
					int numLines = 2;
					String[] words2 = lines[i + 1].split("\\|");

					String street = words1.get(1);

					String zip = words2[0];

					Address adr = new Address(street, city, zip);
					log.info(adr.toString());

					log.info(street + ", " + city + ", " + zip);

					if (adr != null) {

						adrRepo.saveOrUpdate(adr);
						List<Address> adrList = tours.get(tourDate);
						adrList.add(adr);

						TourListEntry tourListEntry = new TourListEntry(tourDate, adr);
						tourListEntry.setCity(city);
						tourListEntry.setStreet(street);
						tourListEntry.setZip(zip);
						setTourListEntryFields(tourListEntry, words1, words2);
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


	public void removeExtraFields(List<String> words1) {

		// ensure the city is the first field
		// rename words
		String city = null;
		int j = 0;
		while (j < words1.size()) {
			city = getCity(words1.get(j));
			if (city != null) {
				for (int i=0; i<j; i++) {
					words1.remove(i);
				}
				break;
			}
			j++;
		}
		
		

		// skip one word is the first word does not look like a
		// street
		if (words1.size() > 1 && words1.get(1) != null && StringUtils.isAllUpperCase(words1.get(1)) && words1.get(1).length() <= 5) {
			// assume it's the second part of the city and skip
			// move the city to the second field and remove the first
			words1.remove(1);
		}
	}

	private void setTourListEntryFields(TourListEntry tourListEntry, List<String> words1, String[] words2) {

		int l1 = words1.size();
		String description = words1.get(l1 - 1);
		tourListEntry.setDescription(description);

		String price = getPrice(words1);
		tourListEntry.setPrice(price);

		String cross = getCrossStreet(words1);
		tourListEntry.setCrossStreet(cross);

		String agent = words2[1];
		tourListEntry.setAgent(agent);

		String mlsNo = getMlsNo(words2);

		tourListEntry.setMlsNo(mlsNo);

	}

	private String getMlsNo(String[] words2) {
		String mlsNo = words2[words2.length - 1];
		if (StringUtil.isNumeric(mlsNo)) {
			return mlsNo;
		}
		return null;
	}

	private String getCrossStreet(List<String> words1) {

		for (int i = 0; i < words1.size()-1; i++) {
			String cross = words1.get(i);
			if (cross.trim().startsWith("@")) {
				if (words1.get(i + 1) != null) {
					return words1.get(i + 1);
				}
			}
		}
		return null;

	}

	private String getPrice(List<String> words1) {
		for (String p : words1) {
			if (p.trim().startsWith("$")) {
				return p;
			}
		}
		return null;
	}

	private String getCity(String line) {
		String city = line.trim();
		switch (city) {
		case "PIED":
			return "Piedmont";
		case "OAK":
			return "Oakland";
		case "PLEAS":
			return "Pleasant Hill";
		case "ALA":
			return "Alameda";
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

	private Date getTourDate(List<String> words1) {
		Date date = null;
		// TODO iterate through words of the line
		for (String word : words1) {

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

		String text;
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

	public List<TourListEntry> getTourList(Date tourDate) {
		List<TourListEntry> tleList = tlRepo.findByTourDate(tourDate);
		return tleList;
	}
}
