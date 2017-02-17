package com.afn.realstat;

import static org.junit.Assert.*;

import org.junit.Test;

public class TrialTest {

	@Test
	public void SystemOutTest() {
		System.out.println("Testing System.out.println to show up on console");
	}
	
	@Test
	public void WorkingDirTest() {
		String workingDir = System.getProperty("user.dir");
		System.out.println("Current working directory : " + workingDir);
	}

}
