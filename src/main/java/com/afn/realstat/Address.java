package com.afn.realstat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;
import com.skovalenko.geocoder.address_parser.UnparsedAddress;
import com.skovalenko.geocoder.address_parser.us.UsAddressParser;

import ch.qos.logback.classic.Logger;

public class Address {
	
	
	private static final Logger log = (Logger) LoggerFactory.getLogger("import");

	private static char FIELD_SEP = '|';

	private ParsedUsAddress parsedAddress;
	private String rawAddress;
	
	final static Map<String, String> streetNameTranslator;
    static {
        Map<String, String> snt = new HashMap<String,String>();
        snt.put("PLEASANT", "PLEASANT VALLEY");
        streetNameTranslator = Collections.unmodifiableMap(snt);
    }
	
	public Address() {
	};

	// This constructor is primarily used by property_transaction since the unit
	// number is in a separate field
	public Address(String inStreet, String unit, String city, String inZip) {
		
		String street = inStreet;
		String zip = inZip;
		
		if (unit != null && !unit.isEmpty()) {
			street += " #" + unit;
		}
		setRawAddress(street + ", " + city + ", " + zip);
		
		// if the zip has a 4-digit extension remove it because the parser does not deal with it
		String[] zipParts = zip.split("-");
		if (zipParts.length ==2) {
			zip = zipParts[0];
		}

		UnparsedAddress upa = new UnparsedAddress(street, city, zip);
		UsAddressParser parser = new UsAddressParser();
		parsedAddress = parser.parse(upa);
		fixSpecialCases();
	}

	// This constructor is primarily used by realProperty because the unit
	// number is included in the streetUnit field
	public Address(String streetUnit, String city, String zip) {
		this(streetUnit, null, city, zip);
	}

	private void fixSpecialCases() {
		String newStreetName = streetNameTranslator.get(this.parsedAddress.getStreetName());
		if (newStreetName != null) {
			parsedAddress.setStreetName(newStreetName);
		}
	}

	public String getRawAddress() {
		return rawAddress;
	}

	public void setRawAddress(String rawAddress) {
		this.rawAddress = rawAddress;
	}

	/*
	 * cleanAddress contains no street type or unit numbers
	 */
	public String getCleanAddress() {
		StringBuffer sb = new StringBuffer();
		sb.append(parsedAddress.getStreetNumber());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getStreetPreDir());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getStreetName());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getStreetPostDir());
		sb.append(FIELD_SEP);
		// sb.append(parsedAddress.getSubUnitName());
		sb.append(parsedAddress.getSubUnitNumber());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getCity());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getZip());
		// debug TODO remove
		if (parsedAddress.getZip()== null || parsedAddress.getZip().isEmpty() ) {
			log.warn("Clean address zip code is empty or null");
		}
		return sb.toString();
	}

	public String toString() {
		return parsedAddress.toString();
	}

	public boolean hasUnitInfo() {
		String unitNbr = parsedAddress.getSubUnitNumber();
		if (unitNbr != null && !unitNbr.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isInSameBuilding(Address adr) {
		if (parsedAddress.getStreetName().equals(adr.parsedAddress.getStreetName())
				&& parsedAddress.getStreetNumber().equals(adr.parsedAddress.getStreetNumber())
				&& parsedAddress.getCity().equals(adr.parsedAddress.getCity())
				&& parsedAddress.getZip().equals(adr.parsedAddress.getZip())) {
			return true;
		}
		return false;
	}

}
