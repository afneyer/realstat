package com.afn.realstat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MyTour {

	private Date tourDate;
	private List<TourListEntry> tourList;
	private ArrayList<TourListEntry> selectedList;
	private int[] sequence;

	public MyTour(Date tourDate, TourListRepository tlRepo) {
		this.tourDate = tourDate;
		tourList = tlRepo.findByTourDate(tourDate);
		selectedList = new ArrayList<TourListEntry>();
	}

	public boolean selectEntry(TourListEntry tourListEntry) {
		boolean changed = false;
		if (! selectedList.contains(tourListEntry)) {
			changed = selectedList.add(tourListEntry);
		}
		return changed;
	}

	public boolean deselectEntry(TourListEntry tourListEntry) {
		boolean changed = false;
		if (selectedList.contains(tourListEntry)) {
			changed = selectedList.remove(tourListEntry);
		}
		return changed;
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

	public List<TourListEntry> getTourList() {
		// TODO Auto-generated method stub
		return tourList;
	}
	
	public List<Address> getSelectedAddresses() {
		List<Address> list = new ArrayList<Address>();
		
		for (TourListEntry tle : selectedList) {
			list.add(tle.getPropertyAdr());
		}
		return list;
		
	}
	
	public List<TourListEntry> getSelected() {
		return selectedList;
	}

	public int[] getSequence() {
		return sequence;
	}

	public void setSequence(int[] order) {
		this.sequence = order;
		for (int i=0; i<order.length; i++) {
			TourListEntry tle = selectedList.get(i);
			tle.setSequence(i);
		}
	}

	public void clearSequence() {
		for (TourListEntry tle : selectedList) {
			tle.setSequence(0);
		}
	}

}
