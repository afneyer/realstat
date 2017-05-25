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
	
	private Button printButton;
	private PdfFileGetter pdfFileGetter;
/*
	ShowPdfButton(String caption) {
		setCaption(caption);
		printButton = this;
		
		this.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

				File file = pdfFileGetter.getPdfFile();
				
				// Create the stream resource and give it a file name
				// set the path to write the temporary pdf to
				String filePath = System.getProperty("user.dir") + "\\Downloads\\";
				
				PdfSource source = new PdfSource(file);
				StreamResource resource = new StreamResource(source, filePath);

				// These settings are not usually necessary. MIME type
				// is detected automatically from the file name, but
				// setting it explicitly may be necessary if the file
				// suffix is not ".pdf".
				resource.setMIMEType("application/pdf");
				resource.getStream().setParameter("Content-Disposition", "attachment; filename=" + filePath);

				// Extend the print button with an opener
				// for the PDF resource
				BrowserWindowOpener opener = new BrowserWindowOpener(resource);
				opener.extend(printButton);
				// printButton.markAsDirty();

				// Save the results and ensure that the document is properly
				// closed:
			}
		});

	}
	*/
	
	ShowPdfButton() {
		printButton = this;
		new EnhancedBrowserWindowOpener()
	    .clientSide(true)
	    .withGeneratedContent("myFileName.pdf", this::getStreamSource)
	    .doExtend(this);
	}

	public InputStream getStreamSource() {
		File file = pdfFileGetter.getPdfFile();
		
		// Create the stream resource and give it a file name
		// set the path to write the temporary pdf to
		String filePath = System.getProperty("user.dir") + "\\Downloads\\";
		
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
