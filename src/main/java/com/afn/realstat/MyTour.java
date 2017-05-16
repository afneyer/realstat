package com.afn.realstat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.afn.realstat.ui.ShowPdfButton.PdfDocumentGetter;

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
			tle.setSequence(order[i]+1);
		}
	}

	public void clearSequence() {
		for (TourListEntry tle : selectedList) {
			tle.setSequence(0);
		}
	}
	
	public File getPdfFile() {
		
		PDDocument printDoc = new PDDocument();
		PDPage page = new PDPage();
		printDoc.addPage( page );

		// Create a new font object selecting one of the PDF base fonts
		PDFont font = PDType1Font.HELVETICA_BOLD;

		// Start a new content stream which will "hold" the to be created content
		
		File file = null;
		PDPageContentStream contentStream;
		try {
			contentStream = new PDPageContentStream(printDoc, page);
			// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
			contentStream.beginText();
			contentStream.setFont( font, 12 );
			contentStream.moveTextPositionByAmount( 100, 700 );
			contentStream.drawString( "Hello World" );
			contentStream.endText();

			// Make sure that the content stream is closed:
			contentStream.close();
			
			// Save the results and ensure that the document is properly closed:
			String filePath = System.getProperty("user.dir") + "\\temp\\";
			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss.sss");
			format.format(new Date());
			String fileName =format.format(new Date()) + ".pdf";
			file = new File(filePath + fileName);
			printDoc.save(file);
			printDoc.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;
	}


}
