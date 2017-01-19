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
public class ImportAllTest {
	 
	 @Autowired
	 private MlsImporter mlsImporter;
	 
	 @Autowired
	 private CrsImporter crsImporter;
	 
	 @Autowired
	 private AgentVolumeImporter agvImporter;
	 
	 @Test
		public void testImportFile() {
		 
		 	File file;
		    // 
			// file = new File("C:\\afndev\\apps\\realstat\\data","MLSExport - 94611_0-1500.csv");
			// mlsImporter.importFile(file);
			// file = new File("C:\\afndev\\apps\\realstat\\data","CRSPropertyExport - 94611_0-1500.csv");
			// crsImporter.importFile(file);
			file = new File("C:\\afndev\\apps\\realstat\\data","Paragon-Volume-Ranking---Agent-Within-MLS-2012-01-12 - 2017-01-12.csv");
			agvImporter.importFile(file);
		}
}
