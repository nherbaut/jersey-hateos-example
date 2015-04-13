package fr.labri.utils;

import java.util.Random;

import com.google.common.io.BaseEncoding;

public class Utils {
	private static final Random random = new Random(); // or SecureRandom

	public static String getRandomString() {
		final byte[] buffer = new byte[5];
		random.nextBytes(buffer);
		return BaseEncoding.base64Url().omitPadding().encode(buffer); // or
																		// base32()
	}

}
