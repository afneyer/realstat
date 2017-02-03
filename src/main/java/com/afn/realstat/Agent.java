package com.afn.realstat;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

@Entity
@Table(name = "agent", uniqueConstraints = @UniqueConstraint(columnNames = { "license" }),
		indexes = {
		@Index(name = "idx_firstName", columnList = "firstName"),
		@Index(name = "idx_firstName", columnList = "lastName"),
		@Index(name = "idx_license", columnList = "license") } )
public class Agent extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("import");

	private String agentName;	
	private String userCode;
	@Basic(optional = false)
	private String firstName;
	private String middleName;
	private String middleInitial;
	@Basic(optional = false)
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
	
	public Agent(String license, String agentNameRaw) {
		this.license = license;
		this.agentName = agentNameRaw;
		clean();
	}

	public Agent(String license, String firstName, String lastName) {
		this.license = license;
		this.agentName = firstName + " " + lastName;
		clean();
	}

	
	public void clean() {
		cleanUserCode();
		extractFirstMiddleLast();
		cleanLicense();
	}

	private void cleanLicense() {
		if (license != null && !license.isEmpty()) {
			license = license.replaceAll("[A-Za-z]", "");
			license = StringUtils.stripStart(license, "0");
		}
	}

	// TODO remove
	private void cleanUserCode() {
		if (userCode != null) {
			userCode = userCode.toLowerCase();
		}
	}

	private void extractFirstMiddleLast() {
		PersName pn = new PersName(agentName);
		this.firstName = pn.getFirstName();
		this.lastName = pn.getLastName();
		this.middleName = pn.getMiddleName();
		this.middleInitial = pn.getMiddleInitial();
	}

	@Override
	public String toString() {
		return String.format("Agent [License='%s', Name='%s', FirstName='%s', LastName='%s', MiddleInitial='%s']", 
				license, agentName, firstName, lastName, middleInitial);
	}

	@Override
	public void saveOrUpdate() {
	}

	@Override
	public Example<AbstractEntity> getRefExample() {
		Agent sampleAgent = new Agent();
		sampleAgent.firstName = firstName;
		sampleAgent.lastName = lastName;
		sampleAgent.license = license;
		Example<AbstractEntity> e = Example.of(sampleAgent);

		return e;
	}
	
	@Override
	public boolean isValid() {
		return (firstName != null && lastName != null && license != null);
	}

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
