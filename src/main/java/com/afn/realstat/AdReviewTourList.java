package com.afn.realstat;

import java.io.File;
import java.io.IOException;
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

	public static final Logger log = LoggerFactory.getLogger("log");

	private File file;
	private Map<Date, List<Address>> tours = new HashMap<Date, List<Address>>();
	private AddressRepository adrRepo;

	public AdReviewTourList() {
	};

	public AdReviewTourList(File file, AddressRepository adrRepo) {
		this.adrRepo = adrRepo;
		this.file = file;
		if (file == null) {
			this.file = new File("C:\\afndev\\apps\\realstat\\testdata", "17-03-25_Tour.pdf");
		}
		createTourList();
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
	public void createTourList() {
		String text;
		try {
			text = getText(file);

			// System.out.println("Text in PDF: " + text);
			String[] lines = text.split(System.lineSeparator());
			tours = new HashMap<Date, List<Address>>();

			for (String line : lines) {
				System.out.println("|" + line + "|");
			}

			Date tourDate = null;
			int i = 0;

			while (i < lines.length) {
				String line = lines[i];

				int processedLines = 0;

				Date tmpTourDate = getTourDate(line);
				if (tmpTourDate != null) {
					if (!tmpTourDate.equals(tourDate)) {
						tourDate = tmpTourDate;
						System.out.println("- dat: " + tourDate);
						tours.put(tourDate, new ArrayList<Address>());
					} else {
						System.out.println("- dat: " + tourDate + " found same tour date again");
					}
				}

				String city = getCity(line);
				if (city != null) {
					int numFields = getNumFields(i, lines);

					String[] adrArray = Arrays.copyOfRange(lines, i, i + numFields);
					Address adr = getAddress(adrArray);
					if (adr != null) {
						adrRepo.saveOrUpdate(adr);
						List<Address> adrList = tours.get(tourDate);
						adrList.add(adr);
					}
					processedLines = numFields;
				}

				if (processedLines == 0) {
					processedLines = 1;
				}

				i += processedLines;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private int getNumFields(int i, String[] lines) {
		int j = i + 1;
		String line = null;
		while (j < lines.length) {
			line = lines[j];
			if (getCity(line) != null) {
				return j - i;
			}

			if (getTourDate(line) != null) {
				return j - i;
			}

			if (getFooter(line) != null) {
				return j - i;
			}

			j++;
		}
		if (j >= lines.length) {
			System.out.println("Got to the end of the text lines in the file");
		}
		return 1;
	}

	private String getFooter(String line) {
		if (line.toLowerCase().contains("ad review")) {
			return line;
		}
		return null;
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

	private Date getTourDate(String line) {

		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd, yyyy");
		try {
			date = formatter.parse(line);
			return date;
		} catch (ParseException e) {
			return null;
		}
	}

	private String getText(File pdfFile) throws IOException {
		PDDocument doc = PDDocument.load(pdfFile);
		return new PDFTextStripper().getText(doc);
	}
}
