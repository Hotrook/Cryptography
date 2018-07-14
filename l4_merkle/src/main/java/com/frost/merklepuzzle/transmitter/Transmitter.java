package com.frost.merklepuzzle.transmitter;

import com.frost.merklepuzzle.encryption.EncryptionException;
import com.frost.merklepuzzle.encryption.Encryptor;
import com.frost.merklepuzzle.puzzler.Puzzler;

public class Transmitter {

	private Puzzler generator;
	private Puzzler receiver;

	public Transmitter(Puzzler generator, Puzzler receiver) {
		this.generator = generator;
		this.receiver = receiver;
	}

	public void transmit(byte[] ciphertext) throws EncryptionException {
		receiver.receiveMessage(ciphertext);
	}

	public void transmitBack(byte[] id) throws EncryptionException {
		generator.receiveBack(id);
	}

	public void transmit(long N, int n, Encryptor enc) {
		receiver.receiveInit(N, n, enc);
	}
}
