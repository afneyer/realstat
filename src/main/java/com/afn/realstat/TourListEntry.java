package com.afn.realstat;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

@Entity
// TODO
@Table(name = "tourlist_entry", indexes = {
		@Index(name = "idx_tourDate", columnList = "tourDate")})



public class TourListEntry extends AbstractEntity {
	
	public static final Logger log = LoggerFactory.getLogger("app");
	public static final Class<TourListEntry> classType = TourListEntry.class;

	@Basic(optional = false)
	private Date tourDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Address propertyAdr;
	
	private String city;
	private String zip;
	private String street;
	private String crossStreet;
	private String bedBath;
	private String price;
	private String time;
	private String description;
	private String agent;
	private String office;
	private String phone;
	private String mlsNo;
	
	public TourListEntry() {
	}

	public TourListEntry(Date tourDate, String[] entryElements) {
		this.tourDate = tourDate;		
	}

	public TourListEntry(Date tourDate, Address adr) {
		this.tourDate = tourDate;
		this.propertyAdr = adr;
	}

	@Override
	public Example<TourListEntry> getRefExample() {
		TourListEntry sample = new TourListEntry();
		sample.tourDate = tourDate;
		sample.propertyAdr = propertyAdr;
		Example<TourListEntry> e = Example.of(sample);
		return e;
	}

	@Override
	public boolean isValid() {
		return propertyAdr != null;
	}

	@Override
	public void clean() {
	}

	public Date getTourDate() {
		return tourDate;
	}

	public void setTourDate(Date tourDate) {
		this.tourDate = tourDate;
	}

	public Address getPropertyAdr() {
		return propertyAdr;
	}

	public void setPropertyAdr(Address propertyAdr) {
		this.propertyAdr = propertyAdr;
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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCrossStreet() {
		return crossStreet;
	}

	public void setCrossStreet(String crossStreet) {
		this.crossStreet = crossStreet;
	}

	public String getBedBath() {
		return bedBath;
	}

	public void setBedBath(String bedBath) {
		this.bedBath = bedBath;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMlsNo() {
		return mlsNo;
	}

	public void setMlsNo(String mlsNo) {
		this.mlsNo = mlsNo;
	}
	
	@Override
	public String toString() {
		String str = "TourListEntry" + tourDate + " - " + propertyAdr.toString();
		return str;
	}

}
