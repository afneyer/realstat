package com.afn.realstat.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.vaadin.server.StreamResource.StreamSource;

@SuppressWarnings("serial")
public class PdfSource implements StreamSource {

	private PDDocument pdfDoc;

	public PdfSource(PDDocument pdfDoc) {
		this.pdfDoc = pdfDoc;
	}

	@Override
	public InputStream getStream() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			pdfDoc.save(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] temp = out.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(temp);
		return in;
	}
}
