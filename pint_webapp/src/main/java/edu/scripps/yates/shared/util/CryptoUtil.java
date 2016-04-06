package edu.scripps.yates.shared.util;

import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;

public class CryptoUtil {
	public static final String KEY = "NWEKRMSSksdfo389234n/*23r4f";

	public static String encrypt(String text) {
		if (text == null)
			return null;
		TripleDesCipher cipher = new TripleDesCipher();
		cipher.setKey(getDefaultKey());
		try {
			String enc = cipher.encrypt(String.valueOf(text));
			return enc;
		} catch (DataLengthException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (InvalidCipherTextException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String encodedText) {
		if (encodedText == null)
			return null;
		TripleDesCipher cipher = new TripleDesCipher();
		cipher.setKey(getDefaultKey());
		try {
			String dec = cipher.decrypt(encodedText);
			return dec;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] getDefaultKey() {
		return new byte[] { (byte) 4, (byte) 8, (byte) 3, (byte) 80, (byte) 12, (byte) -9, (byte) -5, (byte) 101,
				(byte) 15, (byte) -4, (byte) 1, (byte) 0, (byte) 90, (byte) -9, (byte) 55, (byte) -41, (byte) -9,
				(byte) 40, (byte) 3, (byte) 10, (byte) -40, (byte) 39, (byte) 50, (byte) 112 };
	}
}
