package com.afn.realstat;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfNull;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

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
		if (!selectedList.contains(tourListEntry)) {
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
		for (int i = 0; i < order.length; i++) {
			TourListEntry tle = selectedList.get(i);
			tle.setSequence(order[i] + 1);
		}
	}

	public void clearSequence() {
		for (TourListEntry tle : selectedList) {
			tle.setSequence(0);
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

	/*
	 * TODO remove
	 * 
	 * public File getPdfFileUsingPDocument() {
	 * 
	 * PDDocument printDoc = new PDDocument(); PDPage page = new PDPage();
	 * printDoc.addPage(page);
	 * 
	 * // Create a new font object selecting one of the PDF base fonts PDFont
	 * font = PDType1Font.HELVETICA_BOLD;
	 * 
	 * // Start a new content stream which will "hold" the to be created //
	 * content
	 * 
	 * File file = null; PDPageContentStream contentStream; try { contentStream
	 * = new PDPageContentStream(printDoc, page); // Define a text content
	 * stream using the selected font, moving the // cursor and drawing the text
	 * "Hello World" contentStream.beginText(); contentStream.setFont(font, 16);
	 * 
	 * // Write the date String date = this.getTourDate().toString(); date +=
	 * "\n"; contentStream.showText(date); contentStream.endText();
	 * 
	 * List<TourListEntry> tourList = this.getSelected();
	 * 
	 * int pageSize = 5; for (int i = 0; i < tourList.size(); i++) {
	 * 
	 * TourListEntry tle = tourList.get(i);
	 * 
	 * // if necessary create new page if ((i + 1) % pageSize == 0) {
	 * 
	 * page = new PDPage(); printDoc.addPage(page); contentStream = new
	 * PDPageContentStream(printDoc, page); }
	 * 
	 * this.pdfTourListEntry(tle, contentStream);
	 * 
	 * }
	 * 
	 * // Make sure that the content stream is closed: contentStream.close();
	 * 
	 * // Save the results and ensure that the document is properly closed:
	 * String filePath = System.getProperty("user.dir") + "\\temp\\";
	 * SimpleDateFormat format = new
	 * SimpleDateFormat("yyyy-mm-dd-hh-mm-ss.sss"); format.format(new Date());
	 * String fileName = format.format(new Date()) + ".pdf"; file = new
	 * File(filePath + fileName); printDoc.save(file); printDoc.close();
	 * 
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * return file; }
	 */

	private void addPdfTourList(Document doc) {
		addPageHeader(doc);
		addTourList(doc);
	}

	private void addPageHeader(Document doc) {
		
		SimpleDateFormat format = new SimpleDateFormat("EEEE,  MMMM dd, yyyy");
		
		String dateStr = format.format(getTourDate());
		String userName = "Kathleen Callahan";
		String title = "Tour for " + userName + " on " + dateStr;
		doc.add(new Paragraph(title).setBold().setFontSize(20));

	}

	private void addTourList(Document doc) {

		// review this code for breaks
		int pageSize = 5;
		for (int i = 0; i < tourList.size(); i++) {

			TourListEntry tle = tourList.get(i);

			// if necessary create new page
			if ((i + 1) % pageSize == 0) {

				doc.add(new Paragraph("here would be a break TODO"));
			}

			addTourListEntry(tle, doc);
			addTourListSpacer(doc);

		}

	}
	
	private Document addTourListSpacer(Document doc) {
		doc.add(new Paragraph("This is a horizontal line plus space afterwards"));
		doc.add(new Paragraph("space"));
		return doc;
	}
	
	private PdfFont getFont(String str) {
		
		try {
			switch(str) {
				case "normal" :
					return PdfFontFactory.createFont(FontConstants.HELVETICA);
				case "bold" :
					return PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
				case "oblique":
					return PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE);
				case "obliqueBold":
					return PdfFontFactory.createFont(FontConstants.HELVETICA_BOLDOBLIQUE);
				default:
					return PdfFontFactory.createFont(FontConstants.HELVETICA);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void addTourListEntry(TourListEntry tle, Document doc) {

		// add table for holding a tour list entry
		UnitValue[] unitArray = { new UnitValue(UnitValue.PERCENT, 5f), new UnitValue(UnitValue.PERCENT, 10f), new UnitValue(UnitValue.PERCENT, 23f),
				new UnitValue(UnitValue.PERCENT, 22f), new UnitValue(UnitValue.PERCENT, 18f),
				new UnitValue(UnitValue.PERCENT, 22f) };

	
		Table table = new Table(unitArray);
		Cell cell = null;
		// add the first row of cells
		// Cell 0
		cell = new Cell().setBold().setFontSize(12);
		cell.add(new Integer(tle.getSequence()).toString());
		table.addCell(cell);
		
		// Cell 1
		cell = new Cell().setFont(getFont("normal")).setFontSize(12);
		cell.add(tle.getCity());
		table.addCell(cell);
		
		// Cell 2-3
		cell = new Cell(1,2).setFont(getFont("bold")).setFontSize(12);
		cell.add(tle.getStreet() + "@" + tle.getCrossStreet());
		table.addCell(cell);
		
		
		// Cell 4
		cell = new Cell().setFont(getFont("bold")).setFontSize(12).setTextAlignment(TextAlignment.RIGHT);
		cell.add(tle.getBedBath());
		table.addCell(cell);
		
		// Cell 5
		cell = new Cell().setFont(getFont("bold")).setFontSize(12).setTextAlignment(TextAlignment.RIGHT);
		cell.add(tle.getPrice());
		table.addCell(cell);
		
		table.startNewRow();
		
		// add second row of cells
		table.startNewRow();
		table.addCell("");
		table.addCell("");
		cell = new Cell(1,4).setFontSize(10);
		cell.add(tle.getDescription());
		table.addCell(cell);
		
		// add third row of cells
		table.startNewRow();
		table.addCell("");
		table.addCell(tle.getZip());
		table.addCell(tle.getAgent());
		table.addCell(tle.getOffice());
		table.addCell(tle.getPhone());
		table.addCell("MLS# " + tle.getMlsNo());

		doc.add(table);
	}

}
