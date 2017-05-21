package com.afn.realstat.ui;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;

@SuppressWarnings("serial")
public class ShowPdfButton extends Button {

	private PDDocument pdfDoc;

	ShowPdfButton(PdfFileGetter pdfFileGetter) {
		
		Button printButton = this;

		setCaption("Show File");

		this.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

				File file = pdfFileGetter.getPdfFile();
				
				PdfSource source = new PdfSource(file);

				// Create the stream resource and give it a file name
				String filePath = file.getAbsolutePath();
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

				// Save the results and ensure that the document is properly
				// closed:
			}
		});

	}

	public interface PdfFileGetter {
		public File getPdfFile();
	}
};
