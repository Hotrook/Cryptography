package com.frost.cpaaes.encryption;

public class EncryptorOFB extends Encryptor {

	public static String cipherType = "AES/OFB/PKCS5Padding";

	public EncryptorOFB() throws EncryptionException {
		super(cipherType);
	}
}
