package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.Test;

public class CsvFileWriterTest {

	@Test
	public void testFileWriter() {

		String fileName = "C:\\afndev\\apps\\realstat\\logs\\testoutput\\testFileWriter.txt";
		String header = "FirstName, LastName, City, Zip";

		CsvFileWriter cfw = new CsvFileWriter(fileName, header);
		String line1 = "Andreas, Neyer, Oakland, 94611";
		String line2 = "Kathleen, Callahan, Piedmont, 94610";
		cfw.appendLine(line1);
		cfw.appendLine(line2);
		cfw.close();

		// read file back for test
		try {
			FileReader r = new FileReader(fileName);
			BufferedReader br = new BufferedReader(r);
			String line;
			int count = 0;
			while ((line = br.readLine()) != null) {
				switch (count) {
				case 0:
					assertEquals(header, line);
					break;
				case 1:
					assertEquals(line1, line);
					break;
				case 2:
					assertEquals(line2, line);
					break;
				}
				count++;
			}
			assertEquals(3, count);

			br.close();
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

}
