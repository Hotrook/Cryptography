package com.frost.merklepuzzle.encryption;

public interface Encryptor {

	byte[] encrypt(byte[] message, byte[] k) throws EncryptionException;

	byte[] generateKey(int n) throws EncryptionException;

	byte[] decrypt(byte[] key, byte[] ciphertext) throws EncryptionException;

	byte[] createKey(long k, int n);
}

