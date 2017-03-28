package com.afn.realstat.sandbox;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;

public class PdfReadToTextTest {

	
	@Test
	public void samplePdfExtractTest() {
		try {
			File file = new File("C:\\afndev\\apps\\realstat\\testdata","17-03-25_Tour.pdf");
		    String text = getText(file);
		    System.out.println("Text in PDF: " + text);
		} catch (IOException e) {
		    e.printStackTrace();
		}    
	}
	
	static String getText(File pdfFile) throws IOException {
	    PDDocument doc = PDDocument.load(pdfFile);
	    return new PDFTextStripper().getText(doc);
	}
}
