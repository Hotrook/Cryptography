package com.frost.cpaaes.encryption;

public class EncryptorCTR extends Encryptor {

	public static String cipherType = "AES/CTR/PKCS5Padding";

	public EncryptorCTR() throws EncryptionException {
		super(cipherType);
	}

}
