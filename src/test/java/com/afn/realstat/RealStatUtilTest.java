package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.supercsv.cellprocessor.ParseDouble2;

public class RealStatUtilTest {


	@Test
	public void testCleanApn() {
		
		assertEquals("48d730422", RealStatUtil.cleanApn("48D-7304-22"));
		assertEquals("12987253", RealStatUtil.cleanApn("12-987-253"));
		assertEquals("48g7448133", RealStatUtil.cleanApn("48G-7448-13-3"));
		assertEquals("100815085", RealStatUtil.cleanApn("10081508500"));
		assertEquals(null, RealStatUtil.cleanApn(null));
		assertEquals(null, RealStatUtil.cleanApn("NOT FOUND IN PUB"));
		assertEquals("48e732287", RealStatUtil.cleanApn("048E732208700"));
		
		ParseDouble2 parser = new ParseDouble2();

		assertEquals("2.0", parser.cleanStrDouble2("2.0"));
		assertEquals(null, parser.cleanStrDouble2("MONTCL"));
		assertEquals("3", parser.cleanStrDouble2("*3"));
		
	}

}
