package com.afn.realstat.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.realstat.Application;
import com.afn.realstat.Customer;
import com.afn.realstat.CustomerRepository;
import com.afn.util.QueryResultTable;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class QueryResultTableTest {

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
	public void testStringTableConstructorAndAccessors() {

		cRepo.save(new Customer("Andreas", "Neyer"));
		cRepo.save(new Customer("Kathleen", "Callahan"));

		String query = "select id, firstName, lastName from customer order by id";
		QueryResultTable st = new QueryResultTable(afnDataSource, query);

		assertEquals(3, st.getColumnCount());
		assertEquals(2, st.getRowCount());

		assertEquals("id", st.getHeaderElement(1));
		assertEquals("firstName", st.getHeaderElement(2));
		assertEquals("lastName", st.getHeaderElement(3));

		assertEquals("Andreas", st.get(0, 1));
		assertEquals("Neyer", st.get(0, 2));
		assertEquals("Kathleen", st.get(1, 1));
		assertEquals("Callahan", st.get(1, 2));

	}
	
	@Test
	public void testStringTableSetters() {

		cRepo.save(new Customer("Andreas", "Neyer"));
		cRepo.save(new Customer("Kathleen", "Callahan"));

		String query = "select id, firstName, lastName from customer order by id";
		QueryResultTable st = new QueryResultTable(afnDataSource, query);
		
		String[] row = {"54321","James","Miller"};
		st.addRow(row);
	    assertEquals(st.getRowCount(),3);
	    assertEquals("Miller",st.get(2,2));
		
		String[] column = {"middleInitial","F","M","X"};
		st.addColumn(column);
		assertEquals(st.getColumnCount(),4);
	    assertEquals("X",st.get(2,3));
	    assertEquals("middleInitial",st.getHeaderElement(3));
	    
	    try {
	    	String[] row1 = {"54321","James"};
			st.addRow(row1);
		} catch ( Exception e ) {
			System.out.println(e.getMessage());
			assertTrue( e.getMessage().startsWith("Row must have same number") );
		}
	    
	    try {
	    	String[] col1 = {"middleInitial","F","M","X","Z"};
			st.addColumn(col1);
		} catch ( Exception e ) {
			System.out.println(e.getMessage());
			assertTrue( e.getMessage().startsWith("Column must have same number"));
		}
	    
	    try {
			st.set(2, 5, "invalidColumn");
		} catch ( Exception e ) {
			System.out.println(e.getMessage());
			assertTrue( e.getMessage().startsWith("ColumnIndex for a QueryResultTable"));
		}

		try {
		st.set(4, 3, "invalidRow");
		}  catch ( Exception e ) {
			System.out.println(e.getMessage());
			assertTrue( e.getMessage().startsWith("RowIndex for a QueryResultTable"));
		}

	}
}
