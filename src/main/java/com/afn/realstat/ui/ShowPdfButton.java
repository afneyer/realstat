package com.afn.realstat.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;

import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;

@SuppressWarnings("serial")
public class ShowPdfButton extends Button  {
	
	private PdfFileGetter pdfFileGetter;

	
	ShowPdfButton() {
		new EnhancedBrowserWindowOpener()
	    .clientSide(true)
	    .withGeneratedContent("myFileName.pdf", this::getStreamSource)
	    .doExtend(this);
	}

	public InputStream getStreamSource() {
		File file = pdfFileGetter.getPdfFile();
		
		// Create the stream resource and give it a file name
		// set the path to write the temporary pdf to
		// String filePath = System.getProperty("user.dir") + "\\Downloads\\";
		
		PdfSource src = new PdfSource(file);
		
		return src.getStream();
	}
	
	
	
	public void setPdfFileGetter( PdfFileGetter pdfFileGetter ) {
		this.pdfFileGetter = pdfFileGetter;
	}

	public interface PdfFileGetter {
		public File getPdfFile();
	}
};
