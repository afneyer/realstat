package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

// @RunWith(SpringRunner.class)
// @SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)

@ActiveProfiles("prod")
public class PdfBoxTests {

	@Test
	public void testPDFTestStripper() {

		File file = new File("C:\\afndev\\apps\\realstat\\testdata", "17-03-25_Tour.pdf");

		String text = getText(file);
		System.out.print(text);

	}

	@Test
	public String getText(File file) {

		String text = null;
		try {
			PDDocument doc = PDDocument.load(file);
			PDFTextStripper pdfStrip = new PDFTextStripper();
			pdfStrip.setArticleStart("");
			pdfStrip.setArticleEnd("^");
			pdfStrip.setParagraphStart("");
			pdfStrip.setParagraphEnd("\n");
			pdfStrip.setWordSeparator("|");
			pdfStrip.setLineSeparator("|");
			pdfStrip.setSortByPosition(true);

			text = pdfStrip.getText(doc);

			System.out.println("|" + pdfStrip.getArticleStart() + "|");
			System.out.println("|" + pdfStrip.getArticleEnd() + "|");
			System.out.println("|" + pdfStrip.getParagraphStart() + "|");
			System.out.println("|" + pdfStrip.getParagraphEnd() + "|");
			System.out.println("|" + pdfStrip.getWordSeparator() + "|");

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return text;

	}

}
