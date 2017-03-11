package com.afn.realstat;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.data.domain.Example;
import org.springframework.data.geo.Point;

import com.afn.util.MapLocation;

@Entity
@Table(name = "address", indexes = { @Index(name = "idx_streetNbr", columnList = "streetNbr"),
		@Index(name = "idx_streetName", columnList = "streetName") })
public class Address extends AbstractEntity {

	private String streetNbr;
	private String streetPreDir;
	private String streetName;
	private String streetType;
	private String streetPostDir;
	private String unitNbr;
	private String unitName;
	private String city;
	private String state;
	private String zip;
	private String zip4;
	private String county;
	private String country;
	private Point location;

	public Address() {
	}

	public Address(String inStreet, String inUnit, String inCity, String inZip) {
		AddressParser adrPrsr = new AddressParser(inStreet, inUnit, inCity, inZip);
		setFields(adrPrsr);
	}

	public Address(String inStreetUnit, String inCity, String inZip) {
		this(inStreetUnit, null, inCity, inZip);
	}

	private void setFields(AddressParser prsdAdr) {
		setStreetNbr(prsdAdr.getStreetNbr());
		setStreetPreDir(prsdAdr.getStreetPreDir());
		setStreetName(prsdAdr.getStreetName());
		setStreetType(prsdAdr.getStreetType());
		setStreetPostDir(prsdAdr.getStreetPostDir());
		setUnitNbr(prsdAdr.getSubUnitNumber());
		setUnitName(prsdAdr.getSubUnitName());
		setCity(prsdAdr.getCity());
		setState(prsdAdr.getState());
		setZip(prsdAdr.getZip());
		setZip4(prsdAdr.getZip4());
		setCounty(prsdAdr.getCounty());
		setCountry(prsdAdr.getCountry());
		// setMapLocationFields();
	}

	
	public void setMapLocationFields() {
		if (location == null) {
			MapLocation loc = new MapLocation(this.toString());
			if (true /*!loc.getPartialMatch()*/) {
				location = loc.getLocation();
				state = loc.getState();
				zip4 = loc.getZip4();
				county = loc.getCounty().toUpperCase();
				
				// overwrite and correct
				zip = loc.getZip();
				city = loc.getCity().toUpperCase();
			/*} else {
				System.out.println("Partial Match for" + this); */
			}
		}
	}

	public String getFullStreet() {
		StringBuffer sb = new StringBuffer();
		sb.append(streetNbr);
		sb.append(" ");
		sb.append(streetPreDir);
		sb.append(" ");
		sb.append(streetName);
		sb.append(" ");
		sb.append(streetPostDir);
		sb.append(" ");
		sb.append(streetType);
		sb.append(" ");
		sb.append(unitName);
		sb.append(unitNbr);
		String fs = sb.toString();
		fs = fs.trim();
		fs = fs.replaceAll(" +", " ");
		return fs;
	}

	public String toString() {
		String adrStr = getFullStreet();
		adrStr += ", " + city;
		adrStr += "," + zip;
		if (zip4 != null) {
			adrStr += "-" + zip4;
		}
		return adrStr;

	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {
		return true;
	}

	public String getStreetNbr() {
		return streetNbr;
	}

	public void setStreetNbr(String streetNbr) {
		this.streetNbr = streetNbr;
	}

	public String getStreetPreDir() {
		return streetPreDir;
	}

	public void setStreetPreDir(String streetPreDir) {
		this.streetPreDir = streetPreDir;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetType() {
		return streetType;
	}

	public void setStreetType(String streetType) {
		this.streetType = streetType;
	}

	public String getStreetPostDir() {
		return streetPostDir;
	}

	public void setStreetPostDir(String streetPostDir) {
		this.streetPostDir = streetPostDir;
	}

	public String getUnitNbr() {
		return unitNbr;
	}

	public void setUnitNbr(String unitNbr) {
		this.unitNbr = unitNbr;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getZip4() {
		return zip4;
	}

	public void setZip4(String zip4) {
		this.zip4 = zip4;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Point getLocation() {
		return location;
	}

	@Override
	public Example<Address> getRefExample() {
		Address adr = new Address();
		adr.streetNbr = streetNbr;
		adr.streetName = streetName;
		adr.streetPreDir = streetPreDir;
		adr.streetPostDir = streetPostDir;
		adr.unitNbr = unitNbr;
		adr.unitName = unitName;
		adr.zip = adr.zip;
		adr.city = adr.city;

		Example<Address> e = Example.of(adr);
		return e;
	}

}
