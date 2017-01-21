package com.afn.realstat;

public class Address {
	
	
	private String street;
	private String unit;
	private String city;
	private String zip;
	
	public Address (String street, String unit, String city, String zip) {
		this.street = street;
		this.unit = unit;
		this.city = city;
		this.zip = zip;
	}
	
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	

}
