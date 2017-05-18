package com.afn.realstat;

import java.util.Date;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;

import com.afn.realstat.framework.SpringApplicationContext;

public class MyTourStop {

	public static final Logger log = LoggerFactory.getLogger("app");
	public static final Class<MyTourStop> classType = MyTourStop.class;
	public static TourListRepository repo;

	private TourListEntry tle;
	private MyTour tour;
	private int sequence;

	public MyTourStop() {
	}

	public MyTourStop(MyTour tour, TourListEntry tle) {
		this.tle = tle;
		this.tour = tour;
	}

	public String htmlString() {
		String html = "";
		html += "<p style=\"text-align:left;\">";
		html += getStreet() + ", " + getCity();
		html += "<span style=\"float:right;\">";
		html += "<bold>";
		html += getPrice();
		html += "</bold>";
		html += "</span>";
		html += "<br>";
		html += getDescription();
		html += "<br>";
		html += getAgent();
		html += "<span style=\"float:right;\">";
		html += "MLS# ";
		html += getMlsNo();
		html += "</span>";
		html += "</p>";
		return html;
	}

	// todo remove
	public String htmlString02() {
		String html = "";
		html += "<p>";
		html += getStreet() + ", " + getCity() + ", ";
		html += "<bold>";
		html += getPrice();
		html += "</bold>";
		html += "</p>";
		html += "<p>";
		html += getDescription();
		html += "</p>";
		html += "<p>";
		html += getAgent();
		html += "</p>";
		return html;

	}

	public Date getTourDate() {
		return tle.getTourDate();
	}

	public void setTourDate(Date tourDate) {
		this.tle.setTourDate(tourDate);
	}

	public MyTour getTour() {
		return tour;
	}

	public Address getPropertyAdr() {
		return tle.getPropertyAdr();
	}

	@Deprecated
	public void setPropertyAdr(Address propertyAdr) {
		// this.propertyAdr = propertyAdr;
	}

	public Point getLocation() {
		return getPropertyAdr().getLoc();
	}

	public String getCity() {
		return tle.getCity();
	}

	@Deprecated
	public void setCity(String city) {
		// this.city = city;
	}

	public String getZip() {
		return tle.getZip();
	}

	@Deprecated
	public void setZip(String zip) {
		// this.zip = zip;
	}

	public String getStreet() {
		return tle.getStreet();
	}

	@Deprecated
	public void setStreet(String street) {
		// this.street = street;
	}

	public String getCrossStreet() {
		return tle.getCrossStreet();
	}

	@Deprecated
	public void setCrossStreet(String crossStreet) {
		// this.crossStreet = crossStreet;
	}

	public String getBedBath() {
		return tle.getBedBath();
	}

	@Deprecated
	public void setBedBath(String bedBath) {
		// this.bedBath = bedBath;
	}

	public String getPrice() {
		return tle.getPrice();
	}

	@Deprecated
	public void setPrice(String price) {
		// this.price = price;
	}

	public String getTime() {
		return tle.getTime();
	}

	@Deprecated
	public void setTime(String time) {
		// this.time = time;
	}

	public String getDescription() {
		return tle.getDescription();
	}

	@Deprecated
	public void setDescription(String description) {
		// this.description = description;
	}

	public String getAgent() {
		return tle.getAgent();
	}

	@Deprecated
	public void setAgent(String agent) {
		// this.agent = agent;
	}

	public String getOffice() {
		return tle.getOffice();
	}

	@Deprecated
	public void setOffice(String office) {
		// this.office = office;
	}

	public String getPhone() {
		return tle.getPhone();
	}

	@Deprecated
	public void setPhone(String phone) {
		// this.phone = phone;
	}

	public String getMlsNo() {
		return tle.getMlsNo();
	}

	@Deprecated
	public void setMlsNo(String mlsNo) {
		// this.mlsNo = mlsNo;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getStringSeq() {
		if (sequence == 0) {
			return "";
		} else {
			return Integer.toString(sequence);
		}
	}

	@Override
	public String toString() {
		String str = "MyTourListStop: " + getTourDate() + " - " + getPropertyAdr().toString();
		return str;
	}

	public TourListRepository getRepo() {
		if (repo == null) {
			repo = (TourListRepository) SpringApplicationContext.getBean("tourListRepository");
		}
		return repo;
	}

	public String mapMarkerCaption() {

		int width = 50;

		String desc = WordUtils.wrap(getDescription(), width, "\n", false);
		String s = "";
		String sc = getStreet() + " @ " + getCrossStreet();
		String cz = getCity() + ", " + getZip() + "\n";
		s += sc + cz;
		s += getTime() + "\n";
		s += desc + "\n";
		s += getAgent() + "  " + getOffice() + "\n";
		s += "MLS# " + getMlsNo();
		return s;

	}

}
