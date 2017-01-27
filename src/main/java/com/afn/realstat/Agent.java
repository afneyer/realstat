package com.afn.realstat;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

@Entity
@Table(name = "agent", uniqueConstraints = @UniqueConstraint(columnNames = { "UserCode" }))
public class Agent extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("import");

	@Basic(optional = false)
	private String agentName;
	@Basic(optional = false)
	private String userCode;
	private String firstName;
	private String middleName;
	private String middleInitial;
	private String lastName;
	private String officeName;
	private String officeCode;
	private String officePhone;
	private String type;
	private String status;
	@Basic(optional = false)
	private String license;

	public Agent() {
	}

	public Agent(String userCode, String agentName) {
		this(userCode, agentName, null);
	}

	public Agent(String userCode, String agentName, String license) {
		this.userCode = userCode;
		this.agentName = agentName;
		this.license = license;
		cleanUpFields();
	}

	private void cleanUpFields() {
		cleanUserCode();
		extractFirstMiddleLast();
		cleanLicense();
	}

	private void cleanLicense() {
		if (license != null && !license.isEmpty()) {
			license = license.replaceAll("[A-Za-z]", "");
			license = StringUtils.stripStart(license,"0");	
		}
	}

	private void cleanUserCode() {
		userCode = userCode.toLowerCase();
	}

	private void extractFirstMiddleLast() {

		// split by comma
		String[] nameParts = agentName.split(",");
		// TODO process non-comma formats
		if (nameParts.length == 2) {
			
			lastName = WordUtils.capitalize(StringUtils.trim(nameParts[0]).toLowerCase());

			// split by blank
			String firstMiddle = nameParts[1];
			firstMiddle = firstMiddle.replaceAll("  ", " ");
			firstMiddle = StringUtils.trim(firstMiddle);
			String[] nameParts2 = firstMiddle.split(" ");

			switch (nameParts2.length) {
			case 1:
				firstName = WordUtils.capitalizeFully(StringUtils.trim(nameParts2[0]));
				middleName = null;
				middleInitial = null;
				break;
			case 2:
				firstName = WordUtils.capitalizeFully(StringUtils.trim(nameParts2[0]));
				middleName = WordUtils.capitalizeFully(StringUtils.trim(nameParts2[1]));
				cleanMiddleName();
				extractMiddleInitial();
				break;
			default:
				log.warn("Unparseable Agent Name " + agentName);
				break;
			}

		} else {
			log.warn("Not yet implemented Parse Agent Name " + agentName);
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

	@Override
	public String toString() {
		return String.format("Agent [User Code='%s', Name='%s', License='%s']", userCode, agentName, license);
	}

	@Override
	public void saveOrUpdate() {
	}

	@Override
	public Example<AbstractEntity> getRefExample() {
		Example<AbstractEntity> e = Example.of(new Agent(getUserCode(), getAgentName()));
		return e;
	}

	/*
	 * public String extractBrefromAgentRaw() { String agr = getAgentRaw();
	 * String bre = agr.replaceAll("[A-Z,a-z,\\-, ]",""); return bre; }
	 */

	// All getters and setters

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
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

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

}
