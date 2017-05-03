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
	private File pdfFile;

	public AdReviewTourList() {
	};

	public AdReviewTourList(File file, AddressRepository adrRepo, TourListRepository tlRepo) {
		this.adrRepo = adrRepo;
		this.tlRepo = tlRepo;
		this.pdfFile = file;
	}

	public void createTourList() {
		String text = getText();
		// createTourList(text);
		createTourListBasedOnFields(text);
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
					// TODO find the next city code because the number of lines
					// per entry is not consistently 2
					int numLines = 2;
					String[] words2 = lines[i + 1].split("\\|");

					String street = words1.get(1);

					String zip = words2[0];

					Address adr = new Address(street, city, zip);
					adr.setState("CA");
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

	public void createTourListBasedOnFields(String text) {

		// Cleanup text

		// Remove all new line markers. They seem to be less reliable than the
		// field markers "|"
		text = text.replaceAll("\\n", " ");

		// Ensure that the @-sign is in a separate field
		text = text.replaceAll(" @", "|@");
		text = text.replaceAll("@ ", "@|");

		// Split Ad Review into fields
		List<String> allFields = Arrays.asList(text.split("\\|"));

		// System.out.println("Text in PDF: " + text);
		// String[] lines = text.split(System.lineSeparator());
		HashMap<Date, List<String>> tourDays = splitByDates(allFields);

		List<List<String>> tourElements = new ArrayList<List<String>>();
		for (Date tourDate : tourDays.keySet()) {

			List<String> tourDayFields = tourDays.get(tourDate);

			tourElements.addAll(splitIntoTourStops(tourDayFields));
			int numStops = tourElements.size();

			// process all tourElements
			for (List<String> tourElement : tourElements) {

				// process tourElements
				TourListEntry te = parseTourElement(tourElement);
				te.setTourDate(tourDate);
				te.save();

			}
		}
	}

	private HashMap<Date, List<String>> splitByDates(List<String> allFields) {

		HashMap<Date, List<String>> tourDays = new HashMap<Date, List<String>>();

		ArrayList<String> tourDay = null;
		Date date = null;
		Date previousDate = null;
		for (String field : allFields) {
			field = field.trim();
			date = getTourDate(field);
			if (date != null) {
				// found a new date
				if (date != previousDate) {
					previousDate = date;
					tourDay = new ArrayList<String>();
					tourDays.put(date, tourDay);
				}
			} else {
				// all non-date fields get added to the list
				if (tourDay != null) {
					tourDay.add(field);
				}
			}
		}
		return tourDays;

	}

	private TourListEntry parseTourElement(List<String> tourElement) {

		int cityIndex = 0;
		int priceIndex = getPriceIndex(tourElement);
		int streetIndex = 1;
		int crossStreetIndex = getCrossStreetIndex(tourElement);
		int descriptionIndex = getDescriptionIndex(tourElement);
		int zipIndex = getZipIndex(tourElement);
		int mlsTagIndex = getMlsTagIndex(tourElement);
		int agentIndex = getZipIndex(tourElement) + 1;
		int officeIndex = getOfficeIndex(tourElement);
		String bedBath = getBedBath(tourElement);

		TourListEntry tourListEntry = new TourListEntry();
		tourListEntry.setCity(tourElement.get(cityIndex));
		tourListEntry.setZip(getFieldForIndex(tourElement, zipIndex));
		tourListEntry.setStreet(getFieldForIndex(tourElement, streetIndex));
		tourListEntry.setCrossStreet(getFieldForIndex(tourElement, crossStreetIndex));
		tourListEntry.setBedBath(bedBath);
		tourListEntry.setPrice(getFieldForIndex(tourElement, priceIndex));
		tourListEntry.setDescription(tourElement.get(descriptionIndex));
		tourListEntry.setAgent(tourElement.get(agentIndex));
		
		// office
		String office = tourElement.get(officeIndex);
		office = cleanOffice(office);
		tourListEntry.setOffice(office);

		// phone number
		String phone = tourElement.get(getMlsTagIndex(tourElement) - 1);
		phone = cleanPhone(phone);
		tourListEntry.setPhone(phone);

		String mlsNo = null;
		if (mlsTagIndex + 1 >= tourElement.size()) {
			mlsNo = null;
		} else {
			mlsNo = tourElement.get(mlsTagIndex + 1);
			mlsNo = mlsNo.replaceAll("[^\\d]", "");
			Long mlsNoLong;
			try {
				mlsNoLong = new Long(mlsNo);
				if (mlsNoLong > 10000000) {
					mlsNo = mlsNo.trim();
					tourListEntry.setMlsNo(mlsNo);
				} else {
					mlsNo = null;
				}
			} catch (NumberFormatException e) {
				mlsNo = null;
			}

		}
		
		Address propertyAddress = new Address(tourListEntry.getStreet(), tourListEntry.getCity(), tourListEntry.getZip());
		propertyAddress.saveOrUpdate();
		tourListEntry.setPropertyAdr(propertyAddress);
		return tourListEntry;
	}

	private String cleanPhone(String phone) {
		phone = phone.replaceAll("[^\\d-]", "");
		return phone;
	}

	private String cleanOffice(String office) {
		office = office.replaceAll("[\\d-]", "");
		return office;
	}

	private String getFieldForIndex(List<String> tourElement, int index) {
		String str = "";
		if (index == 0) {
			return str;
		} else {
			return tourElement.get(index);
		}

	}

	private int getPriceIndex(List<String> tourElement) {

		for (int i = 0; i < tourElement.size(); i++) {
			String field = tourElement.get(i);
			if (field.contains("$")) {
				field = field.replaceAll(",", "");
				field = field.replaceAll("[$]", "");
				if (StringUtils.isNumeric(field)) {
					return i;
				}
			}
		}
		log.warn("Parsing Tour List Element : " + "Cannot find Price for " + printTourElement(tourElement));
		return 0;
	}

	private int getCrossStreetIndex(List<String> tourElement) {

		for (int i = 0; i < tourElement.size(); i++) {
			String field = tourElement.get(i);
			field.trim();
			if (field.equals("@")) {
				return i + 1;
			}
		}
		log.warn("Parsing Tour List Element : " + "Cannot find CrossStreet marker @ for "
				+ printTourElement(tourElement));
		return 0;
	}

	private int getDescriptionIndex(List<String> tourElement) {

		int index = getPriceIndex(tourElement);
		int stringLen = 0;
		for (int i = getPriceIndex(tourElement); i < getZipIndex(tourElement); i++) {
			String field = tourElement.get(i);
			if (field.length() > stringLen) {
				stringLen = field.length();
				index = i;
			}
		}
		if (index == getPriceIndex(tourElement)) {
			log.warn("Parsing Tour List Element : " + "Cannot find description for " + printTourElement(tourElement));
		}
		return 0;
	}

	private int getZipIndex(List<String> tourElement) {

		for (int i = 0; i < tourElement.size(); i++) {
			String field = tourElement.get(i);
			field = field.trim();
			field = field.replaceAll("\\n", "");
			if (field.startsWith("94") && StringUtils.isNumeric(field)) {
				return i;
			}
		}

		log.warn("Parsing Tour List Element : " + "Cannot find zip for " + printTourElement(tourElement));
		return 0;
	}

	private int getMlsTagIndex(List<String> tourElement) {

		for (int i = 0; i < tourElement.size(); i++) {
			String field = tourElement.get(i);
			field = field.trim();
			if (field.startsWith("MLS#")) {
				if (i < tourElement.size()) {
					return i;
				}
			}
		}

		log.warn("Parsing Tour List Element : " + "Cannot find MLS# tag for " + printTourElement(tourElement));
		return 0;
	}

	private int getOfficeIndex(List<String> tourElement) {
		int index = getMlsTagIndex(tourElement) - 2;
		if (index > 0) {
			return index;
		} else {
			log.warn("Parsing Tour List Element : " + "Cannot find Office " + printTourElement(tourElement));
			return 0;
		}
	}

	private String getBedBath(List<String> tourElement) {
		String bb = new String();
		int start = getCrossStreetIndex(tourElement);
		int end = getPriceIndex(tourElement);
		for (int i = start + 1; i < end; i++) {
			bb += tourElement.get(i);
		}
		return bb;
	}

	private String printTourElement(List<String> tourElement) {
		String printStr = "|";
		for (String e : tourElement) {
			printStr += e + "|";
		}
		return printStr;
	}

	private List<List<String>> splitIntoTourStops(List<String> tourDayFields) {

		List<List<String>> tourStops = new ArrayList<List<String>>();
		List<String> tourStop = null;
		for (String field : tourDayFields) {

			String city = getCity(field);
			if (city != null) {
				// found the beginning of a tour stop

				// add the previous tour stop
				if (tourStop != null) {
					tourStops.add(tourStop);
				}
				tourStop = new ArrayList<String>();
				tourStop.add(city);
			} else {
				if (tourStop != null) {
					tourStop.add(field);
				}
			}
		}

		// add the last tourStop
		if (tourStop != null) {
			tourStops.add(tourStop);
		}

		return tourStops;
	}

	public void removeExtraFields(List<String> words1) {

		// ensure the city is the first field
		// rename words
		String city = null;
		int j = 0;
		while (j < words1.size()) {
			city = getCity(words1.get(j));
			if (city != null) {
				for (int i = 0; i < j; i++) {
					words1.remove(i);
				}
				break;
			}
			j++;
		}

		// skip one word is the first word does not look like a
		// street
		if (words1.size() > 1 && words1.get(1) != null && StringUtils.isAllUpperCase(words1.get(1))
				&& words1.get(1).length() <= 5) {
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

		for (int i = 0; i < words1.size() - 1; i++) {
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
		case "E.C.":
			return "El Ceritto";
		case "ALB":
			return "Albany";
		case "EMERY":
			return "Emeryville";
		default:
			log.error("Parsing Ad Review List Error: Cannot find city tag for " + city);
			return null;
		}
	}

	private Date getTourDate(List<String> words1) {
		Date date = null;
		// TODO iterate through words of the line
		for (String word : words1) {

			date = getTourDate(word);
		}
		return date;
	}

	private Date getTourDate(String field) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd, yyyy");
		try {
			date = formatter.parse(field);
			return date;
		} catch (ParseException e) {
		}
		return date;
	}

	public String getText() {

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

			text = pdfStrip.getText(doc);

			System.out.println("|" + pdfStrip.getArticleStart() + "|");
			System.out.println("|" + pdfStrip.getArticleEnd() + "|");
			System.out.println("|" + pdfStrip.getParagraphStart() + "|");
			System.out.println("|" + pdfStrip.getParagraphEnd() + "|");
			System.out.println("|" + pdfStrip.getWordSeparator() + "|");

			// ensure that there is a line separation before the zip code
			// text = text.replaceAll("\\|94", "\n\\|94");
			// text = text.replaceAll("\n\n", "\n");

			text = ensureLineBreakAfterPrice(text);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return text;
	}

	private String ensureLineBreakAfterPrice(String text) {

		String[] fields = text.split("\\|");
		ArrayList<String> list = new ArrayList<String>();
		for (String field : fields) {
			list.add(field);
			if (field.startsWith("$")) {
				String sep = "\n";
				list.add(sep);
			}
		}

		String resString = StringUtils.join(list, "|");

		return resString;
	}

	public List<TourListEntry> getTourList(Date tourDate) {
		List<TourListEntry> tleList = tlRepo.findByTourDate(tourDate);
		return tleList;
	}
}
