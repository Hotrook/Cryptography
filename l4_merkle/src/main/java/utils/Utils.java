package utils;

import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;

public class Utils {

	public static String toBinaryLongString(long n) {
		String binary = Long.toBinaryString(n);
		String result = StringUtils.leftPad(binary, 64, '0');

		return result;
	}

	public static byte[] toBinaryLongStringBytes(long n) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(n);
		byte[] result = buffer.array();
		return buffer.array();
	}
}
