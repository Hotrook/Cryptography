package com.frost.merklepuzzle.puzzler;

import com.frost.merklepuzzle.encryption.EncryptionException;
import com.frost.merklepuzzle.encryption.Encryptor;
import com.frost.merklepuzzle.transmitter.Transmitter;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

import static com.frost.merklepuzzle.utils.Utils.toBinaryBytes;
import static com.frost.merklepuzzle.utils.Utils.toBinaryString;
import static java.lang.Math.abs;

public class Puzzler {

	private long N;
	private int n;
	private int receivedMessages;
	private Transmitter transmitter;
	private Encryptor enc;
	private byte[] idGeneratorKey;
	private byte[] idKey;
	private byte[] toBreak;
	private long toBreakId;

	private Logger log = LoggerFactory.getLogger(Puzzler.class);

	/**
	 * This is for puzzler which receives ciphertexts and chooses id
	 */
	public Puzzler() {

	}

	/**
	 * @param n   Number of bits for key. Number of messages may be calculated as N = 2^n
	 * @param enc is responsible for making encryption
	 */
	public Puzzler(int n, Encryptor enc) {
		this.n = n;
		this.N = (long) 1 << n;
		this.enc = enc;
	}

	public void run() throws EncryptionException {
		transmitter.transmit(N, n, enc);
		generateKeys();

		log.info("Starting sending messages.");
		for (int i = 0; i < N; ++i) {
			sendMessage(i);
		}
		log.info("Sending completed.");
	}

	private void sendMessage(int i) throws EncryptionException {
		byte[] id = enc.encrypt(toBinaryString(i), idGeneratorKey);
		byte[] encryptedId = enc.encrypt(id, idKey);
		byte[] messageKey = enc.generateKey(n);
		byte[] message = makeMessage(id, encryptedId, N);
		byte[] ciphertext = enc.encrypt(message, messageKey);
		log.debug("Sending message {} encrypted with key {}", message, messageKey);
		transmitter.transmit(ciphertext);
	}

	private byte[] makeMessage(byte[] id, byte[] key, long N) {
		byte[] binary = toBinaryBytes(N);
		byte[] result = ArrayUtils.addAll(id, ArrayUtils.addAll(key, binary));

		return result;
	}

	private void generateKeys() throws EncryptionException {
		idGeneratorKey = enc.generateKey(n);
		idKey = enc.generateKey(n);
	}

	public void receiveBack(byte[] id) throws EncryptionException {
		log.info("Received id is: {}", id);
		byte[] key = enc.encrypt(id, idKey);
		log.info("The key is: {}", key);
	}

	public void receiveInit(long N, int n, Encryptor enc) {
		this.N = N;
		this.enc = enc;
		this.n = n;
		Random gen = new Random();
		toBreakId = abs(gen.nextLong()) % N;
		receivedMessages = 0;
	}

	public void receiveMessage(byte[] ciphertext) throws EncryptionException {
		if (receivedMessages == toBreakId) {
			toBreak = ciphertext;
		}
		receivedMessages++;
		if (receivedMessages == N) {
			breakAndSendId();
		}
	}

	public void setTransmitter(Transmitter transmitter) {
		this.transmitter = transmitter;
	}

	private void breakAndSendId() throws EncryptionException {
		byte[] message = decrypt(toBreak);
		byte[] id = extractId(message);
		byte[] key = extractKey(message);

		log.info("Found key is: {}", key);
		log.info("Sending back id: {}", id);

		transmitter.transmitBack(id);
	}

	private byte[] extractKey(byte[] message) {
		return Arrays.copyOfRange(message, 0, 4);
	}

	private byte[] extractId(byte[] message) {
		return Arrays.copyOfRange(message, 4, 8);
	}

	private byte[] decrypt(byte[] toBreak) throws EncryptionException {
		log.info("Starting breaking the ciphertext: {}", toBreak);
		for (long i = 0; i <= N; ++i) {
			byte[] key = enc.createKey(i, n);
			byte[] message = enc.decrypt(toBreak, key);

			logParameters(key, message);

			if (extractN(message) == N) {
				return message;
			}
		}
		throw new RuntimeException("No key seems to be valid.");
	}

	private void logParameters(byte[] key, byte[] message) {
		log.debug("Decrypted message: {} ", message);
		log.debug("with key: {}", key);
	}

	private long extractN(byte[] message) {
		byte[] bytes = Arrays.copyOfRange(message, 8, 16);
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES).put(bytes);
		buffer.flip();
		return buffer.getLong();
	}
}
