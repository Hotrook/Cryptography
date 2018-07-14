package com.frost.merklepuzzle.encryption;

public class EncryptionException extends Exception {

	public EncryptionException(Exception e) {
		super(e);
	}

	public EncryptionException(String s) {
		super(s);
	}
}
