package com.frost.cpaaes.encryption;

public class EncryptionException extends Exception {

	public EncryptionException(Exception e) {
		super(e);
	}

	public EncryptionException(String message) {
		super(message);
	}
}
