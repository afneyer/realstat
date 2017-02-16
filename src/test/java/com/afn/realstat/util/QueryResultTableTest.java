package com.afn.realstat.util;

import static org.junit.Assert.assertEquals;

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

		assertEquals("Andreas", st.get(1, 2));
		assertEquals("Neyer", st.get(1, 3));
		assertEquals("Kathleen", st.get(2, 2));
		assertEquals("Callahan", st.get(2, 3));

	}
}
