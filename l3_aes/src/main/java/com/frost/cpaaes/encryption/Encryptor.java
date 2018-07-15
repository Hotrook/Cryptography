package com.frost.cpaaes.encryption;

import javafx.util.Pair;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class Encryptor {

	public static final String DECRYPTION_EXTENSION = ".dec";
	public static final String ENCRYPTION_EXTENSION = ".enc";

	public static String AES = "AES";
	protected Cipher cipher;

	public static final Logger log = LoggerFactory.getLogger(Encryptor.class);

	public Encryptor(String cipherType) throws EncryptionException {
		log.info("Init Encryptor with mode: {}", cipherType);
		try {
			this.cipher = Cipher.getInstance(cipherType);
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	public void encryptFiles(List<File> files, Key key) throws EncryptionException {
		SecretKey secretKey = new SecretKeySpec(key.getEncoded(), AES);
		try {
			encryptFiles(files, secretKey);
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	protected void encryptFiles(List<File> files, SecretKey secretKey) throws EncryptionException {
		try {
			for (File file : files) {
				byte[] iv = generateIv();
				byte[] message = Files.readAllBytes(file.toPath());
				byte[] encrypted = encrypt(message, secretKey);
				writeOutputToFile(encrypted, iv, file.getAbsoluteFile());
			}
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	public byte[] encrypt(byte[] message, SecretKey secretKey) {
		byte[] output = null;
		try {
			IvParameterSpec ivSpec = new IvParameterSpec(generateIv());
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
			output = cipher.doFinal(message);
		} catch (Exception e) {
			throw new EncryptionException(e);
		} finally {
			return output;
		}
	}

	protected void decryptFiles(List<File> files, SecretKey secretKey) throws EncryptionException, IOException {
		try {
			for (File file : files) {
				byte[] input = Files.readAllBytes(Paths.get(file.getPath()));
				byte[] ciphertext = Arrays.copyOfRange(input, 0, input.length - 24);
				byte[] iv = Arrays.copyOfRange(input, input.length - 24, input.length - 1);
				byte[] encrypted = decrypt(ciphertext, iv, secretKey);
				writeOutputToFile(encrypted, file.getAbsoluteFile());
			}
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	public byte[] decrypt(byte[] ciphertext, byte[] iv, SecretKey secretKey) {
		byte[] output = null;
		try {
			IvParameterSpec ivParams = new IvParameterSpec(Base64.decodeBase64(iv));
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);
			output = cipher.doFinal(Base64.decodeBase64(ciphertext));
		} catch (Exception e) {
			throw new EncryptionException(e);
		} finally {
			return output;
		}
	}

	protected byte[] generateIv() {
		byte[] iv = new byte[cipher.getBlockSize()];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);

		return iv;
	}

	public Pair<String, Integer> challenge(List<String> strings, Key key) {
		Random generator = new Random();
		int b = generator.nextInt(2);
		SecretKey secretKey = new SecretKeySpec(key.getEncoded(), AES);
		byte[] output = null;

		try {
			output = encrypt(strings.get(b).getBytes(), secretKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String result = Base64.encodeBase64String(output).toString();
		return new Pair<>(result, b);
	}

	public static void writeOutputToFile(byte[] output, byte[] iv, File absoluteFile) throws IOException {
		FileOutputStream outputStream = createOutputStreamToFile(absoluteFile, DECRYPTION_EXTENSION);

		outputStream.write((Base64.encodeBase64String(output)).getBytes());
		outputStream.write((Base64.encodeBase64String(iv)).getBytes());
		outputStream.close();
	}

	private static void writeOutputToFile(byte[] output, File absoluteFile) throws IOException {
		FileOutputStream outputStream = createOutputStreamToFile(absoluteFile, ENCRYPTION_EXTENSION);

		outputStream.write(output);
		outputStream.close();
	}

	private static FileOutputStream createOutputStreamToFile(File absoluteFile, String extension)
	throws IOException {
		String filename = absoluteFile + extension;
		File file = new File(filename);
		return new FileOutputStream(file);
	}

}
