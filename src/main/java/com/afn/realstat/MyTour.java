package com.afn.realstat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MyTour {

	private Date tourDate;
	private List<TourListEntry> tourList;
	private ArrayList<TourListEntry> selectedList;

	public MyTour(Date tourDate, TourListRepository tlRepo) {
		this.tourDate = tourDate;
		tourList = tlRepo.findByTourDate(tourDate);
		selectedList = new ArrayList<TourListEntry>();
	}

	public boolean selectEntry(TourListEntry tourListEntry) {
		return selectedList.add(tourListEntry);
	}

	public boolean deselectEntry(TourListEntry tourListEntry) {
		return selectedList.remove(tourListEntry);
	}

	public Date getTourDate() {
		return tourDate;
	}

	public boolean contains(TourListEntry tourListEntry) {
		return tourList.contains(tourListEntry);
	}
	
	public boolean isSelected(TourListEntry tourListEntry) {
		return selectedList.contains(tourListEntry);
	}

	public Collection<TourListEntry> getTourList() {
		// TODO Auto-generated method stub
		return tourList;
	}
	
	public Collection<TourListEntry> getSelected() {
		return selectedList;
	}

}
