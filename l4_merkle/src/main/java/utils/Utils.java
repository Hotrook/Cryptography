package utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
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

	public byte[] makeOffset(byte[] k, Cipher cipher) {
		byte[] result = new byte[cipher.getBlockSize()];

		for (int i = 0; i < cipher.getBlockSize(); ++i) {
			result[i] = 0;
		}

		int start = cipher.getBlockSize() - k.length;
		for (int i = start; i < cipher.getBlockSize(); ++i) {
			result[i] = k[i - start];
		}

		return result;
	}
}
