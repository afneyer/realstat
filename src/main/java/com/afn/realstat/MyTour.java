package com.afn.realstat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.afn.realstat.ui.ShowPdfButton.PdfFileGetter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

public class MyTour implements PdfFileGetter {

	private Date tourDate;
	private ArrayList<MyTourStop> tourList;
	private ArrayList<MyTourStop> selectedList;
	private int[] sequence;

	public MyTour(Date tourDate) {
		TourListRepository tlRepo = TourListEntry.getRepo();
		this.tourDate = tourDate;
		List<TourListEntry> tleList = tlRepo.findByTourDate(tourDate);
		tourList = new ArrayList<MyTourStop>();
		for (TourListEntry tle : tleList) {
			MyTourStop myStop = new MyTourStop(this, tle);
			tourList.add(myStop);
		}

		selectedList = new ArrayList<MyTourStop>();
	}

	public boolean selectEntry(MyTourStop tourListEntry) {
		boolean changed = false;
		if (!selectedList.contains(tourListEntry)) {
			changed = selectedList.add(tourListEntry);
		}
		return changed;
	}

	public boolean deselectEntry(MyTourStop tourListEntry) {
		boolean changed = false;
		if (selectedList.contains(tourListEntry)) {
			changed = selectedList.remove(tourListEntry);
		}
		return changed;
	}

	public Date getTourDate() {
		return tourDate;
	}

	public boolean contains(MyTourStop tourListEntry) {
		return tourList.contains(tourListEntry);
	}

	public boolean isSelected(MyTourStop tourListEntry) {
		return selectedList.contains(tourListEntry);
	}

	public List<MyTourStop> getTourList() {
		return tourList;
	}

	public List<Address> getSelectedAddresses() {
		List<Address> list = new ArrayList<Address>();

		for (MyTourStop mts : selectedList) {
			list.add(mts.getPropertyAdr());
		}
		return list;

	}

	public List<MyTourStop> getSelected() {
		return selectedList;
	}

	public int[] getSequence() {
		return sequence;
	}

	public void setSequence(int[] order) {
		this.sequence = order;
		for (int i = 0; i < order.length; i++) {
			MyTourStop mts = selectedList.get(i);
			mts.setSequence(order[i] + 1);
		}
	}

	public void clearSequence() {
		for (MyTourStop mts : selectedList) {
			mts.setSequence(0);
		}
	}

	/*
	 * Uses iText to create a pdf-file
	 */
	public File getPdfFile() {

		File file = null;
		try {

			// Save the results and ensure that the document is properly closed:
			String filePath = System.getProperty("user.dir") + "\\temp\\";
			
			// Create new temp directory
			// TODO move into AppFiles
			File dir = new File(filePath);
			if ( !dir.exists() ) {
				dir.mkdir();
			} else {
				if ( ! dir.isDirectory() ) {
					dir.mkdir();
				}
			}
			
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss.sss");
			format.format(new Date());
			String fileName = format.format(new Date()) + ".pdf";
			String dest = filePath + fileName;
			file = new File(filePath + fileName);

			// Initialize PDF writer
			PdfWriter writer = new PdfWriter(dest);

			// Initialize PDF document
			PdfDocument pdf = new PdfDocument(writer);

			// Initialize document
			Document document = new Document(pdf);

			addPdfTourList(document);

			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return file;
	}

	private void addPdfTourList(Document doc) {
		addPageHeader(doc);
		addTourList(doc);
	}

	private void addPageHeader(Document doc) {

		SimpleDateFormat format = new SimpleDateFormat("EEEE,  MMMM dd, yyyy");

		String dateStr = format.format(getTourDate());
		String userName = "Kathleen Callahan";
		String title = "Tour for " + userName + " on " + dateStr;
		doc.add(new Paragraph(title).setBold().setFontSize(16));
		addHorizontalLine(doc);

	}

	private void addTourList(Document doc) {

		// review this code for breaks
		int pageSize = 12;
		for (int i = 0; i < selectedList.size(); i++) {

			MyTourStop mts = selectedList.get(i);
			addMyTourStop(mts, doc);

			// if necessary create new page
			if ((i + 1) % pageSize == 0) {

				doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
				addHorizontalLine(doc);
			}

		}

	}

	private void addHorizontalLine(Document doc) {
		Paragraph p = new Paragraph("").setBorderTop(new SolidBorder(1)).setFontSize(10);
		doc.add(p);
	}

	private void addMyTourStop(MyTourStop mts, Document doc) {

		// add table for holding a tour list entry
		UnitValue[] unitArray = { new UnitValue(UnitValue.PERCENT, 12f), new UnitValue(UnitValue.PERCENT, 25f),
				new UnitValue(UnitValue.PERCENT, 25f), new UnitValue(UnitValue.PERCENT, 18f),
				new UnitValue(UnitValue.PERCENT, 20f) };

		Table table = new Table(unitArray);
		table.setMargin(0);
		table.setPadding(-10);
		table.setProperty(Property.BORDER, Border.NO_BORDER);

		Cell cell = null;
		// add the first row of cells

		// Cell 1
		cell = new Cell().setBold().setFontSize(11).setBorder(Border.NO_BORDER).setHeight(15);
		cell.add(new Integer(mts.getSequence()).toString());
		table.addCell(cell);

		// Cell 2-3
		cell = new Cell(1, 2).setBold().setFontSize(11).setBorder(Border.NO_BORDER);
		cell.add(mts.getStreet() + " @ " + mts.getCrossStreet());
		table.addCell(cell);

		// Cell 4
		cell = new Cell().setBold().setFontSize(11).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER)
				.setHeight(15);
		cell.add(mts.getBedBath());
		table.addCell(cell);

		// Cell 5
		cell = new Cell().setBold().setFontSize(11).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)
				.setHeight(15);
		cell.add(mts.getPrice());
		table.addCell(cell);

		table.startNewRow();

		// add second row of cells
		table.startNewRow();

		cell = new Cell().setBold().setFontSize(9).setBorder(Border.NO_BORDER).setHeight(13);
		cell.add(mts.getCity());
		table.addCell(cell);

		cell = new Cell(1, 4).setFontSize(9).setBorder(Border.NO_BORDER).setHeight(13);
		cell.add(mts.getDescription());
		table.addCell(cell);

		// add third row of cells
		table.startNewRow();

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add(mts.getZip());
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add(mts.getAgent());
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add(mts.getOffice());
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add(mts.getPhone());
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1))
				.setTextAlignment(TextAlignment.RIGHT).setHeight(14);
		cell.add("MLS# " + mts.getMlsNo());
		table.addCell(cell);

		doc.add(table);
	}

}
