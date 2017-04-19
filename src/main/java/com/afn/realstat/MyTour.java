package com.afn.realstat;

import java.util.Date;
import java.util.List;

public class MyTour {

	private Date tourDate;
	private List<TourListEntry> tourList;

	public MyTour(Date tourDate, TourListRepository tlRepo) {
		this.tourDate = tourDate;
		tourList = tlRepo.findByTourDate(tourDate);
	}

	public boolean addEntry(TourListEntry tourListEntry) {
		return tourList.add(tourListEntry);
	}

	public boolean removeEntry(TourListEntry tourListEntry) {
		return tourList.remove(tourListEntry);
	}

	public Date getTourDate() {
		return tourDate;
	}

	public boolean contains(TourListEntry tourListEntry) {
		return tourList.contains(tourListEntry);
	}

}
