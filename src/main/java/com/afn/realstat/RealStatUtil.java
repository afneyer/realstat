package com.afn.realstat;

/**
 * @author Andreas
 *
 */

public class RealStatUtil {

	public RealStatUtil() {
	}

	public static String cleanApn(String inStr) {
		if (inStr == null) {
			return inStr;
		}
		String outStr = inStr;
		outStr = outStr.toLowerCase();
		outStr = outStr.replaceAll("-", "");
		outStr = outStr.replaceAll("00$", "");
		outStr = outStr.replaceAll("^0", "");
		
		if (outStr.startsWith("not")) {
			return null;
		}
		return outStr;
	}

}
