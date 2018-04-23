package Transmitter;

import Encryption.Encryptor;
import Puzzler.Puzzler;

public class Transmitter {

	private Puzzler generator;
	private Puzzler receiver;

	public Transmitter(Puzzler generator, Puzzler receiver) {
		this.generator = generator;
		this.receiver = receiver;
	}

	public void transmit(byte[] ciphertext) {
		receiver.receiveMessage(ciphertext);
	}

	public void transmitBack(byte[] id) {
		generator.receiveBack(id);
	}

	public void transmit(long n, Encryptor enc) {
		receiver.receiveInit(n, enc);
	}
}
