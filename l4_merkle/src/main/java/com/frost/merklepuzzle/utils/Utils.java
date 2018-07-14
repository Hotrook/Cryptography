package com.frost.merklepuzzle.utils;

import java.nio.ByteBuffer;

public class Utils {

	public static byte[] toBinaryBytes(long n) {
		return ByteBuffer.allocate(Long.BYTES).putLong(n).array();
	}

	public static byte[] toBinaryString(int i) {
		return ByteBuffer.allocate(Integer.BYTES).putInt(i).array();
	}

}
