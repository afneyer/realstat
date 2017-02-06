package com.afn.realstat;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ImportAllTest {

	@Autowired
	private MlsImporter mlsImporter;

	@Autowired
	private CrsImporter crsImporter;

	@Autowired
	private AgentImporter agentImporter;

	@Autowired
	private AgentVolumeImporter agvImporter;

	@Test
	public void importCrsFiles() {
		File file;
		file = new File("C:\\afndev\\apps\\realstat\\data", "CRSPropertyExport - 94610_0-2000.csv");
		crsImporter.importFile(file);
		file = new File("C:\\afndev\\apps\\realstat\\data", "CRSPropertyExport - 94610_2001-100000.csv");
		crsImporter.importFile(file);
		file = new File("C:\\afndev\\apps\\realstat\\data", "CRSPropertyExport - 94611_0-1500.csv");
		crsImporter.importFile(file);
		file = new File("C:\\afndev\\apps\\realstat\\data", "CRSPropertyExport - 94611_1501-2500.csv");
		crsImporter.importFile(file);
		file = new File("C:\\afndev\\apps\\realstat\\data", "CRSPropertyExport - 94611_2501-100000.csv");
		crsImporter.importFile(file);
		file = new File("C:\\afndev\\apps\\realstat\\data", "CRSPropertyExport - 94618_0-2500.csv");
		crsImporter.importFile(file);
		file = new File("C:\\afndev\\apps\\realstat\\data", "CRSPropertyExport - 94618_2501-100000.csv");
		crsImporter.importFile(file);
	}

	@Test
	public void importAgentFiles() {
		File file;
		file = new File("C:\\afndev\\apps\\realstat\\data", "MLSAllAgentList.txt");
		agentImporter.importFile(file);
	}

	public void importAgentVolumes() {
		File file;
		file = new File("C:\\afndev\\apps\\realstat\\data",
				"Paragon-Volume-Ranking---Agent-Within-MLS-2012-01-12-2017-01-12.csv");
		agvImporter.importFile(file);
	}

	@Test
	public void importMlsFiles() {
		File file;
		file = new File("C:\\afndev\\apps\\realstat\\data", "MLSExport - 94611_0-1500.csv");
		mlsImporter.importFile(file);
	}
}
