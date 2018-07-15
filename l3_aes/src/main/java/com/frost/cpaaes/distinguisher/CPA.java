package com.frost.cpaaes.distinguisher;

import com.frost.cpaaes.encryption.EncryptionException;
import com.frost.cpaaes.encryption.Encryptor;
import com.frost.cpaaes.encryption.EncryptorCBC;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CPA {

	public static int iv = 1;

	private static final String PASSWORD_FILE_PATH = "src/main/resources/password.txt";
	private static final String keystyorePath = "src/main/resources/keystore.jceks";

	private static String password;
	private static String keyId = "aes_key";
	private static KeyStore keystore;
	private static Key key;

	private final static byte[] MESSSAGE_1 = "1234567890ABCDEF".getBytes();
	private final static byte[] MESSSAGE_2 = "ABCDEF1234567890".getBytes();
	private final static byte[] MESSSAGE_3 = "ABC1234567890DEF".getBytes();

	public static final Logger log = LoggerFactory.getLogger(CPA.class);

	public static void main(String[] args) {

		try {
			readKey();
			Encryptor enc = new EncryptorCBC();
			List<String> ciphertexts = new ArrayList();

			while (ciphertexts.size() < 2) {
				String xored1 = xor(MESSSAGE_1, iv);
				String xored2 = xor(MESSSAGE_2, iv);

				logMessages(xored1, xored2);
				Pair<String, Integer> result = enc.challenge(Arrays.asList(xored1, xored2), key);
				log.info("Received result is: {}", result.getKey());
				
				if (!ciphertexts.contains(result.getKey())) {
					ciphertexts.add(result.getKey());
				}
				iv++;
			}

			String xored1 = xor(MESSSAGE_1, iv);
			String xored3 = xor(MESSSAGE_3, iv);
			logMessages(xored1, xored3);

			Pair<String, Integer> result = enc.challenge(Arrays.asList(xored1, xored3), key);
			log.info("Received result is: {}", result.getKey());

			int b = 0;
			if (!ciphertexts.contains(result.getKey())) {
				b = 1;
			}

			if (b == result.getValue()) {
				System.out.println("You're a winner.");
			} else {
				System.out.println("You're a loser.");
			}

		} catch (KeyException e) {
			log.info("There was a problem with accessing the key", e);
		} catch (IOException e) {
			log.info("There was a problem with input/output operations", e);
		} catch (EncryptionException e) {
			log.info("There was a problem with encryption", e);
		} finally {
		}
	}

	private static void logMessages(String xored1, String xored2) {
		log.info("Sending messages to oracle in challenge mode:");
		log.info("First message: {}", xored1);
		log.info("Second message: {}", xored2);
	}

	private static String xor(byte[] message, int iv) {
		byte[] byteIv = ByteBuffer.allocate(16).putInt(iv).array();
		byte[] result = new byte[message.length];

		for (int i = 0; i < message.length; ++i) {
			result[i] = (byte) (message[i] ^ byteIv[i]);
		}

		return new String(result);
	}

	private static void readKey() throws KeyException, IOException {
		try {
			keystore = KeyStore.getInstance("jceks");
			password = (String) Files.lines(Paths.get(PASSWORD_FILE_PATH)).toArray()[0];
			keystore.load(new FileInputStream(keystyorePath), password.toCharArray());
			key = keystore.getKey(keyId, password.toCharArray());
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new KeyException("There was a problem with accessing the key.");
		}
	}
}
