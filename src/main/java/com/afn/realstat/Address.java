package com.afn.realstat;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;
import com.skovalenko.geocoder.address_parser.UnparsedAddress;
import com.skovalenko.geocoder.address_parser.us.UsAddressParser;

public class Address {
	
	private static char FIELD_SEP = '|';
	
	
	@SuppressWarnings("unused")
	private ParsedUsAddress parsedAddress;
		
	public Address () {
	};
	
	public Address(String streetUnit, String city, String zip) {
		UnparsedAddress upa = new UnparsedAddress(streetUnit, city, zip);
		UsAddressParser parser = new UsAddressParser();
		parsedAddress = parser.parse(upa);
	}
	
	public String getCleanAddress () {
		StringBuffer sb = new StringBuffer();
		sb.append(parsedAddress.getFullStreet());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getCity());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getState());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getZip());
		return sb.toString();
	}

	


	
	
	

}
