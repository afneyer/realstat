package com.afn.realstat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class PersonName {

	
	private static final Logger log = (Logger) LoggerFactory.getLogger("import");

	private String rawName;
	private String firstName;
	private String middleName;
	private String middleInitial;
	private String lastName;

	public PersonName() {
	};

	// The raw name is cleaned up by removing multiple blanks, trimming front and back and capitalizing it.
	public PersonName(String rawName) {
		this.rawName = WordUtils.capitalizeFully((StringUtils.trim(rawName.replaceAll("  "," ")).toLowerCase()));
		this.extractFirstMiddleLast();
	}
	
	private void extractFirstMiddleLast() {

		// split by comma
		String[] nameParts = rawName.split(",");
		// TODO process non-comma formats
		if (nameParts.length == 2) {
			
			lastName = WordUtils.capitalizeFully(StringUtils.trim(nameParts[0]).toLowerCase());

			// split by blank
			String firstMiddle = nameParts[1];
			firstMiddle = firstMiddle.replaceAll("  ", " ");
			firstMiddle = StringUtils.trim(firstMiddle);
			String[] nameParts2 = firstMiddle.split(" ");

			switch (nameParts2.length) {
			case 1:
				firstName = StringUtils.trim(nameParts2[0]);
				middleName = null;
				middleInitial = null;
				break;
			case 2:
				firstName = StringUtils.trim(nameParts2[0]);
				middleName = StringUtils.trim(nameParts2[1]);
				cleanMiddleName();
				extractMiddleInitial();
				break;
			default:
				log.warn("Unparseable Agent Name " + rawName);
				break;
			}

		} else {
			log.warn("Not yet implemented Parse Agent Name " + rawName);
		}
		
	}
	
	private void cleanMiddleName() {
		middleName = middleName.replaceAll("[.]","");
		
	}

	private void extractMiddleInitial() {

		if (middleName == null || middleName.isEmpty() ) {
			middleInitial = null;
			return;
		} 
	    if (middleName.length() >= 1) {
	    	middleInitial = middleName.substring(0,1);
	    	return;
	    }
	}
	
	
	public String getRawName() {
		return rawName;
	}

	public void setRawName(String rawName) {
		this.rawName = rawName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
   
}
