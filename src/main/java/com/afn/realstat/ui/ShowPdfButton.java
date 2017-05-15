package com.afn.realstat.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.afn.realstat.Artifact;
import com.afn.realstat.framework.SpringApplicationContext;
import com.mchange.v2.c3p0.impl.NewProxyConnection;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

@SuppressWarnings("serial")
public class ShowPdfButton extends Button {
	
	private PDDocument pdfDoc;
	private String tempDir  = System.getProperty("user.dir") + "\\temp\\";
	
	ShowPdfButton( Lambda l) {
		
		final Button print = new Button("Show Pdf");
		
		print.addClickListener(new ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
	    	
	    	pdfDoc = pdfDocument;
			String title = pdfDoc.getDocumentInformation().getTitle();
	    	
	        StreamSource source = new PdfSource(pdfDoc);

	        // Create the stream resource and give it a file name
	        String filename = "pdf_printing_example.pdf";
	        StreamResource resource =
	                new StreamResource(source, filename);

	        // These settings are not usually necessary. MIME type
	        // is detected automatically from the file name, but
	        // setting it explicitly may be necessary if the file
	        // suffix is not ".pdf".
	        resource.setMIMEType("application/pdf");
	        resource.getStream().setParameter(
	                "Content-Disposition",
	                "attachment; filename="+filename);

	        // Extend the print button with an opener
	        // for the PDF resource
	        BrowserWindowOpener opener =
	                new BrowserWindowOpener(resource);
	        opener.extend(print);

	     // Save the results and ensure that the document is properly closed:
	    }
	});
	
	}
};
