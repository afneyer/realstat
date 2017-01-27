package com.afn.realstat;

/**
 * @author Andreas
 *
 */

public class RealStatUtil {

	public RealStatUtil() {
	}

	public static String cleanApn(String inStr) {
		if (inStr == null || inStr == "0" ) {
			return null;
		}

		String tmpStr = inStr;
		String outStr = "";
		tmpStr = tmpStr.toUpperCase();

		// try to split by dashes
		String[] parts = tmpStr.split("-");

		if (parts.length > 1) {

			// found many parts and assume that the APN - format is
			// dash-separated as in CRS
			// remove leading 0's from each part

			for (int i = 0; i < parts.length; i++) {
				outStr += parts[i].replaceAll("^0+", "");
			}

		} else {

			// there were no dashes in the APN Number
			// replace 3rd 0 if it follows 2 digits
			outStr = tmpStr;
			outStr = outStr.replaceAll("00$", "");
			if (outStr.length() > 3) {
				char c = outStr.charAt(0);
				if (Character.isDigit(c)) {
					c = outStr.charAt(1);
					if (Character.isDigit(c)) {
						c = outStr.charAt(2);
						if (c == '0') {
							outStr = outStr.substring(0, 2) + outStr.substring(3, outStr.length());
						}
					}
				}
				// remove the 3rd last character if it's a 0
				int i = outStr.length() - 3;
				c = outStr.charAt(i);
				int numZero = 0;
				if (c == '0') {
					// also remove the second last character if it's also a 0
					c = outStr.charAt(i+1);
					numZero = 1;
					if (c == '0') {
						numZero = 2;
					}
					outStr = outStr.substring(0, i) + outStr.substring(i + numZero, outStr.length());
				}
			}

			if (outStr.charAt(0) == '0') {
				outStr = outStr.substring(1, outStr.length());
			}
		}

		if (outStr.startsWith("NOT")) {
			return null;
		}

		return outStr;
	}
	
}
