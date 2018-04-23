package Puzzler;

import Encryption.Encryptor;
import Transmitter.Transmitter;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.abs;
import static utils.Utils.toBinaryLongStringBytes;

public class Puzzler {

	private long N;
	private int n;
	private int receivedMessages;
	private PuzzlerMode mode;
	private Transmitter transmitter;
	private Encryptor enc;
	private byte[] k1;
	private byte[] k2;
	private byte[] toBreak;
	private long toBreakId;

	/**
	 * This is for puzzler which receives ciphertexts and chooses id
	 */
	public Puzzler() {

	}

	/**
	 * @param n   Number of bits for key. Number of messages may be calculated as N = 2^n
	 * @param enc This is for puzzler which generate N ciphertexts and sends them to receiver
	 */
	public Puzzler(int n, Encryptor enc) {
		this.n = n;
		this.N = (long) 1 << n;
		this.enc = enc;
	}

	public void run() {
		transmitter.transmit(N, enc);
		generateKeys();

		for (int i = 0; i < N; ++i) {
			if (i % 1000000 == 0) {
				System.out.println(i);
			}
			sendMessage(i);
		}
	}

	private void sendMessage(int i) {
		byte[] id = enc.encrypt(toBinaryString(i), k1);
		byte[] key = enc.encrypt(id, k2);
		byte[] newKey = enc.generateKey(n);
		byte[] message = makeMessage(id, key, N);
		byte[] ciphertext = enc.encrypt(message, newKey);
		transmitter.transmit(ciphertext);
	}

	private byte[] toBinaryString(int i) {

		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		buffer.putInt(i);
		return buffer.array();
	}

	private byte[] makeMessage(byte[] id, byte[] key, long N) {
		byte[] binary = toBinaryLongStringBytes(N);
		byte[] result = ArrayUtils.addAll(id, ArrayUtils.addAll(key, binary));

		return result;
	}

	private void generateKeys() {
		k1 = enc.generateKey(n);
		k2 = enc.generateKey(n);
	}

	public void receiveBack(byte[] id) {
		System.out.printf("Received id is: ");
		printByteArray(id);
		byte[] key = enc.encrypt(id, k2);
		System.out.printf("The key is: ");
		printByteArray(key);
	}

	public void receiveInit(long N, Encryptor enc) {
		this.N = N;
		this.enc = enc;
		Random gen = new Random();
		long random = abs(gen.nextLong());
		toBreakId = random % N;
		receivedMessages = 0;
	}

	public void receiveMessage(byte[] ciphertext) {

		if (receivedMessages == toBreakId) {
			toBreak = ciphertext;
		}
		receivedMessages++;
		if (receivedMessages == N) {
			breakAndSendId();
		}
	}

	private void breakAndSendId() {
		byte[] message = decrypt(toBreak);
		byte[] id = extractId(message);
		byte[] key = extractKey(message);


		System.out.printf("Found key is:", key);
		printByteArray(key);

		System.out.printf("Sending back id:");
		printByteArray(id);

		transmitter.transmitBack(id);
	}

	private void printByteArray(byte[] key) {
		for (byte b : key) {
			int number = b;
			System.out.print(number + ":");
		}
		System.out.println();
	}

	private byte[] extractKey(byte[] message) {
		return Arrays.copyOfRange(message, 0, 4);
	}

	private byte[] extractId(byte[] message) {
		return Arrays.copyOfRange(message, 4, 8);
	}

	private byte[] decrypt(byte[] toBreak) {
		byte[] result = null;
		long i = 0;
		while (result == null) {
			if (i % 1000000 == 0) {
				System.out.println(i);
			}
			byte[] message = enc.decrypt(i, toBreak);
			long extractedN = extractN(message);
			if (extractedN == N) {
				result = message;
			}
			++i;
		}
		return result;
	}

	private long extractN(byte[] message) {
		byte[] bytes = Arrays.copyOfRange(message, 8, 16);
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.put(bytes);
		buffer.flip();//need flip
		return buffer.getLong();
	}

	public void setTransmitter(Transmitter transmitter) {
		this.transmitter = transmitter;
	}
}
