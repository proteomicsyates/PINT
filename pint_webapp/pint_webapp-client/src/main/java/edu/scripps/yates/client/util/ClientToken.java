package edu.scripps.yates.client.util;

public class ClientToken {

	/**
	 * Gets a string token of the client, that is, a unique string of the
	 * client.
	 * 
	 * @return
	 */
	public static String getToken() {
		String randomCodes = String
				.valueOf((int) (Math.random() * (99999 - 1) + 1));
		while (randomCodes.length() < 5) {
			randomCodes = "0" + randomCodes;
		}
		return randomCodes;
	}

}
