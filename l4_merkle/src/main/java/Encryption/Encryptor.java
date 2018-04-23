package Encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

import static utils.Utils.toBinaryLongStringBytes;

public class Encryptor {

	private static String cipherType = "AES/OFB/NoPadding";
	private Cipher cipher;
	private byte[] iv;
	private SecureRandom secureRandom;


	public Encryptor() {
		try {
			this.cipher = Cipher.getInstance(cipherType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		initIv();
		secureRandom = new SecureRandom();
	}

	private void initIv() {
		iv = new byte[cipher.getBlockSize()];
		for (int i = 0; i < cipher.getBlockSize(); ++i) {
			iv[i] = 0;
		}
	}

	public byte[] encrypt(byte[] message, byte[] k) {
		byte[] key = makeOffset(k);
		SecretKey secretKey = new SecretKeySpec(key, "AES");

		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] output = new byte[0];
		try {
			output = cipher.doFinal(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;
	}

	private byte[] makeOffset(byte[] k) {
		byte[] result = new byte[cipher.getBlockSize()];

		for (int i = 0; i < cipher.getBlockSize(); ++i) {
			result[i] = 0;
		}

		int start = cipher.getBlockSize() - k.length;
		for (int i = start; i < cipher.getBlockSize(); ++i) {
			result[i] = k[i - start];
		}

		return result;
	}

	public byte[] generateKey(int n) {
		byte[] result = new byte[n / 8];
		secureRandom.nextBytes(result);

		return result;
	}

	public byte[] decrypt(long k, byte[] ciphertext) {
		byte[] key = makeOffset(toBinaryLongStringBytes(k));
		SecretKey secretKey = new SecretKeySpec(key, "AES");

		IvParameterSpec ivParams = new IvParameterSpec(iv);
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] output = new byte[0];
		try {
			output = cipher.doFinal(ciphertext);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;

	}


}
