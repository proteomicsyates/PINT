package edu.scripps.yates.dbindex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * Dynamic byte buffer utility
 * 
 * @author Adam
 */
public class DynByteBuffer {
	private static int DEFAULT_CAPACITY = 4 * 4; // 1 sequence = 4 * 4 bytes
	private int curSize = 0;
	private int curCapacity = 16;
	private byte[] data;
	private static Logger log = Logger.getLogger(DynByteBuffer.class);
	private static int maxCapacity = 0;

	DynByteBuffer() {
		data = new byte[curCapacity];
	}

	void add(byte[] toAdd) {
		final int toAddLen = toAdd.length;
		if (curCapacity < curSize + toAddLen) {
			int newCapacity = (curCapacity + toAddLen) * 3;
			if (newCapacity > maxCapacity) {
				log.debug("newCapacity= " + newCapacity);
				maxCapacity = newCapacity;
			}
			byte[] newData = new byte[newCapacity];
			System.arraycopy(data, 0, newData, 0, curSize);
			curCapacity = newCapacity;
			data = newData;
		}

		// insert new data
		System.arraycopy(toAdd, 0, data, curSize, toAddLen);
		curSize += toAddLen;
	}

	void clear() {
		curSize = 0;
		curCapacity = DEFAULT_CAPACITY * 4;
		data = new byte[curCapacity];

	}

	int getSize() {
		return curSize;
	}

	byte[] getData() {
		if (data.length % 4 != 0)
			System.out.println("MALO");
		final byte[] copyOfRange = Arrays.copyOfRange(data, 0, curSize);
		if (copyOfRange.length % 4 != 0)
			System.out.println("MALO");
		return copyOfRange;
	}

	public static byte[] toByteArray(int myInteger) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
				.putInt(myInteger).array();
	}

	public static int toInt(byte[] byteBarray) {
		return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN)
				.getInt();
	}

	// public static byte[] toByteArray(float theFloat) {
	// return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
	// .putFloat(theFloat).array();
	// }

	public static byte[] toByteArray(double theDouble) {
		return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN)
				.putDouble(theDouble).array();
	}

	// public static float toFloat(byte[] byteBarray) {
	// return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN)
	// .getFloat();
	// }

	public static double toDouble(byte[] byteBarray) {
		return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN)
				.getDouble();
	}
}
