package com.frost.cpaaes.encryption;

import java.nio.ByteBuffer;

public class EncryptorCBC extends Encryptor {

	public static String cipherType = "AES/CBC/PKCS5Padding";
	public int ivCounter = 1;

	public EncryptorCBC() throws EncryptionException {
		super(cipherType);
	}

	@Override
	protected byte[] generateIv() {
		byte[] iv = ByteBuffer.allocate(cipher.getBlockSize()).putInt(ivCounter).array();
		ivCounter++;

		return iv;
	}

}
