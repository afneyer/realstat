package com.afn.realstat;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MlsImporterTest {
	
	 @Autowired
	 private PropertyTransactionRepository repository;
	 
	 @Autowired
	 private MlsImporter importer;

	@Test
	public void testImportFile() {
		File file = new File("C:\\afndev\\apps\\realstat\\testdata","MLSTransactionExportTest20.csv");

		importer.importFile(file);
	}
}
