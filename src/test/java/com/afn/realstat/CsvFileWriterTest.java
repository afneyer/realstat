package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.util.QueryResultTable;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CsvFileWriterTest {
	
	@Autowired
	private CustomerRepository cRepo;

	@Autowired
	private DataSource afnDataSource;

	/**
	 * Sets up the test fixture. (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		cRepo.deleteAll();
		cRepo.flush();
	}

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
	
	@Test
	public void testFileWriterQueryResult() {
		
		String fileName = "C:\\afndev\\apps\\realstat\\logs\\testoutput\\testFileWriterQueryResult.txt";
		
		cRepo.save(new Customer("Andreas", "Neyer"));
		cRepo.save(new Customer("Kathleen", "Callahan"));

		String query = "select id, firstName, lastName from customer order by id";
		QueryResultTable st = new QueryResultTable(afnDataSource, query);
		
		CsvFileWriter.writeQueryTable(st, fileName);
		
		
	}

}
