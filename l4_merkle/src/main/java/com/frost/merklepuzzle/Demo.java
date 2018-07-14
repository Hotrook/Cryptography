package com.frost.merklepuzzle;

import com.frost.merklepuzzle.encryption.AESEncryptor;
import com.frost.merklepuzzle.encryption.EncryptionException;
import com.frost.merklepuzzle.encryption.Encryptor;
import com.frost.merklepuzzle.puzzler.Puzzler;
import com.frost.merklepuzzle.transmitter.Transmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {

	public static final Logger log = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) throws EncryptionException {
		Encryptor enc = new AESEncryptor();

		Puzzler generator = new Puzzler(8, enc);
		Puzzler receiver = new Puzzler();

		Transmitter transmitter = new Transmitter(generator, receiver);

		generator.setTransmitter(transmitter);
		receiver.setTransmitter(transmitter);

		log.info("Demo start.");
		generator.run();
		log.info("Demo finished.");
	}
}
