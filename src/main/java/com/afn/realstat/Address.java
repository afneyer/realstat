package com.afn.realstat;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.data.domain.Example;
import org.springframework.data.geo.Point;

import com.afn.util.MapLocation;
import com.skovalenko.geocoder.address_parser.ParsedUsAddress;
import com.skovalenko.geocoder.address_parser.UsAddress;

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
	
	public Address(String rawStringAddress) {
		// AddressParser adrPrs = new AddressParser(rawStringAddress);
	}
	
	private void setFields(ParsedUsAddress usAdr) {
		setStreetNbr( usAdr.getStreetNumber());
		setStreetPreDir( usAdr.getStreetPreDir());
		setStreetName( usAdr.getStreetName());
		setStreetType( usAdr.getStreetType());
		setStreetPostDir( usAdr.getStreetPostDir());
		setUnitNbr( usAdr.getSubUnitNumber());
		setUnitName( usAdr.getSubUnitName());
		setCity(usAdr.getCity());
		setState(usAdr.getState());
		setZip(usAdr.getZip());
		setZip4(usAdr.getZip4());
		setCounty(usAdr.getCounty());
		setCountry(usAdr.getCountry());	
		
		setLocation();
	}
	
	private void setLocation() {
		if (location == null) {
			MapLocation loc = new MapLocation(this.toString());
			location = loc.getLocation();
		}
		
	}
	
	public Address(String inStreet, String unit, String city, String inZip) {
		AddressParser adrPrs = new AddressParser(inStreet, unit, city, zip);
		setFields(adrPrs.getParsedUsAddress());
	}
	
	public Address(String inStreetUnit, String city, String inZip) {
		AddressParser adrPrs = new AddressParser(inStreetUnit, city, zip);
		setFields(adrPrs.getParsedUsAddress());
	}
	
	public String getFullStreet() {
		// @TODO
		return streetName;
		
	}
	
	public String toString() {
		String adrStr = getFullStreet();
		adrStr += ", " + city;
		adrStr += "," + zip;
		return adrStr;
		
	}
	
	@Override
	public void clean() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
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
	public Example getRefExample() {
		// TODO Auto-generated method stub
		return null;
	}

}
