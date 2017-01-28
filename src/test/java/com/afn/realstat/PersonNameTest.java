package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.junit.Test;

import javatools.parsers.NameML;

import javax.naming.NameParser;

public class PersonNameTest {

	@Test
	public void nameLibraryTest() {
		
		javatools.parsers.Name.PersonName n = new javatools.parsers.Name.PersonName("Sergio Vieira de Mello");
		String normailzed = n.normalize();
		String first = n.givenName();	
	}
	@Test
	public void testConstructor() {

		PersonName n;
		
		n = new PersonName("Neyer, Andreas");
		assertEquals("Neyer, Andreas", n.getRawName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals(null, n.getMiddleName());
		assertEquals(null, n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersonName("Neyer, Andreas F.");
		assertEquals("Neyer, Andreas F.", n.getRawName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());


		n = new PersonName("Andreas F. Neyer");
		assertEquals("Andreas F. Neyer", n.getRawName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersonName("Neyer, Andreas Felix");
		assertEquals("Neyer, Andreas Felix", n.getRawName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersonName("Andreas Felix Neyer");
		assertEquals("Andreas Felix Neyer", n.getRawName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersonName("NEYER, Andreas    F");
		assertEquals("Neyer, Andreas F", n.getRawName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersonName("   ANDreas f neyer    ");
		assertEquals("Andreas F Neyer", n.getRawName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersonName(" NEYER, ANDREAS    FELIX");
		assertEquals("Neyer, Andreas Felix", n.getRawName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersonName("    Neyer, Andreas fELIX      ");
		assertEquals("Andreas Felix Neyer", n.getRawName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());
	}

}
