package com.afn.realstat.util;

import org.junit.Test;

// @RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class IconTest {

	@Test
	public void testIconBasic() {
		
		Icon icon = new Icon(Icon.MarkerButtonBlue);
		String iconLocation = icon.getIconUrl();		
		// TODO verify URL
		
	}
	
	@Test
	public void testIconText() {
		Icon icon = new Icon(Icon.MarkerButtonGreen);
		icon.addText("33");
		String iconLocation = icon.getIconUrl();
	}
	
	@Test
	public void textIconScale() {
		Icon icon = new Icon(Icon.MarkerButtonRed, "44");
		icon.scale(0.8);
		String iconLocation = icon.getIconUrl();
	}
}
