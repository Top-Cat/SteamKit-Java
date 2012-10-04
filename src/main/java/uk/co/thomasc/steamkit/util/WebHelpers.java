package uk.co.thomasc.steamkit.util;

public class WebHelpers {
	static boolean isUrlSafeChar(char ch) {
		if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9') {
			return true;
		}

		switch (ch) {
			case '-':
			case '.':
			case '_':
				return true;
		}

		return false;
	}

	public static String UrlEncode(String input) {
		return WebHelpers.UrlEncode(input.getBytes());
	}

	public static String UrlEncode(byte[] input) {
		final StringBuilder encoded = new StringBuilder(input.length * 2);

		for (final byte element : input) {
			final char inch = (char) element;

			if (WebHelpers.isUrlSafeChar(inch)) {
				encoded.append(inch);
			} else if (inch == ' ') {
				encoded.append('+');
			} else {
				encoded.append(String.format("%%%02X", element));
			}
		}

		return encoded.toString();
	}
}
