package com.afn.realstat.sandbox;

import org.eclipse.jetty.io.ManagedSelector;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.junit.Test;

public class EvaluatorTest {
	
	@Test
	public void regexMatching() {
		String s = "94708";
		boolean test = s.matches("94[5678]..");
		
		s="10:30-12:30";
		test = s.matches("^[0-9\\-:]{1,12}$");
		System.out.println(test);
		
		s="10abe-12:30";
		test = s.matches("^[0-9\\-:]{1,12}$");
		System.out.println(test);
	}
	
	@Test
	public void temp() {
		BlockingArrayQueue b = null;
		b.poll();
		ManagedSelector p;
	}

}
