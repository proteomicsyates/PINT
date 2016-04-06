package edu.scripps.yates.dbindex;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Indexing util
 * 
 * @author Adam
 */
public class Util {

	private static final Logger logger = Logger.getLogger(Util.class.getName());

	public static String getMd5(String in) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(in.getBytes(), 0, in.length());

			return new BigInteger(1, md5.digest()).toString(16);

		} catch (final NoSuchAlgorithmException e) {
			logger.log(Level.INFO, "Could not calculate md5sum ", e);
			return "";
		}
	}

	public static byte[] getMd5Bytes(String in) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(in.getBytes(), 0, in.length());
			return md5.digest();

		} catch (final NoSuchAlgorithmException e) {
			logger.log(Level.INFO, "Could not calculate md5sum ", e);
			return new byte[1];
		}
	}

	/**
	 * get last N lines from a file (fast)
	 * 
	 * @param file
	 * @param lines
	 * @return
	 */
	public static String tail(File file, int lines) {
		RandomAccessFile fileHandler = null;
		try {
			fileHandler = new java.io.RandomAccessFile(file, "r");
			long fileLength = file.length() - 1;
			StringBuilder sb = new StringBuilder();
			int line = 0;

			for (long filePointer = fileLength; filePointer != -1; filePointer--) {
				fileHandler.seek(filePointer);
				int readByte = fileHandler.readByte();

				if (readByte == 0xA) {
					if (line == lines) {
						if (filePointer == fileLength) {
							continue;
						} else {
							break;
						}
					}
				} else if (readByte == 0xD) {
					line = line + 1;
					if (line == lines) {
						if (filePointer == fileLength - 1) {
							continue;
						} else {
							break;
						}
					}
				}
				sb.append((char) readByte);
			}

			sb.deleteCharAt(sb.length() - 1);
			String lastLine = sb.reverse().toString();
			return lastLine;
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (java.io.IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fileHandler != null) {
				try {
					fileHandler.close();
				} catch (IOException ex) {
					Logger.getLogger(Util.class.getName()).log(Level.SEVERE,
							null, ex);
				}
			}
		}
	}

	/**
	 * Get number of lines in a file (fast)
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static int countLines(String filename) throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(filename));
		int cnt = 0;
		String lineRead = "";
		while ((lineRead = reader.readLine()) != null) {
		}

		cnt = reader.getLineNumber();
		reader.close();
		return cnt;
	}

	static ResidueInfo getResidues(IndexedSequence peptideSequence,
			int seqOffset, int seqLen, String proteinSequence) {
		int protLen = proteinSequence.length();

		final int resLeftI = seqOffset >= Constants.MAX_INDEX_RESIDUE_LEN ? seqOffset
				- Constants.MAX_INDEX_RESIDUE_LEN
				: 0;
		final int resLeftLen = Math.min(Constants.MAX_INDEX_RESIDUE_LEN,
				seqOffset);
		StringBuilder sbLeft = new StringBuilder(
				Constants.MAX_INDEX_RESIDUE_LEN);
		sbLeft.append(proteinSequence
				.substring(resLeftI, resLeftI + resLeftLen));

		final int end = seqOffset + seqLen;
		final int resRightI = end; // + 1;
		final int resRightLen = Math.min(Constants.MAX_INDEX_RESIDUE_LEN,
				protLen - end - 1);
		StringBuilder sbRight = new StringBuilder(
				Constants.MAX_INDEX_RESIDUE_LEN);
		if (resRightI < protLen) {
			sbRight.append(proteinSequence.substring(resRightI, resRightI
					+ resRightLen));
		}

		// add -- markers to fill Constants.MAX_INDEX_RESIDUE_LEN length
		final int lLen = sbLeft.length();
		for (int c = 0; c < Constants.MAX_INDEX_RESIDUE_LEN - lLen; ++c) {
			sbLeft.insert(0, '-');
		}
		final int rLen = sbRight.length();
		for (int c = 0; c < Constants.MAX_INDEX_RESIDUE_LEN - rLen; ++c) {
			sbRight.append('-');
		}

		final String resLeft = sbLeft.toString();
		final String resRight = sbRight.toString();

		return new ResidueInfo(resLeft, resRight);
	}

}
