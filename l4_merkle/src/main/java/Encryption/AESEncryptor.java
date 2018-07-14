package Encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

public class AESEncryptor implements Encryptor {

	private final static String CIPHER_TYPE = "AES/OFB/NoPadding";
	private Cipher cipher;
	private byte[] iv;
	private SecureRandom secureRandom;

	public AESEncryptor() throws EncryptionException {
		try {
			this.cipher = Cipher.getInstance(CIPHER_TYPE);
			this.secureRandom = new SecureRandom();
			this.iv = new byte[cipher.getBlockSize()];
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	@Override
	public byte[] encrypt(byte[] message, byte[] k) throws EncryptionException {
		return encrypt(message, k, Cipher.ENCRYPT_MODE);
	}

	@Override
	public byte[] decrypt(byte[] ciphertext, byte[] k)
	throws EncryptionException {
		return encrypt(ciphertext, k, Cipher.DECRYPT_MODE);
	}

	@Override
	public byte[] generateKey(int n) {
		byte[] result = new byte[n / 8];
		secureRandom.nextBytes(result);

		return result;
	}

	@Override
	public byte[] createKey(long k) {
		return toBinaryBytes(k);
	}

	private byte[] encrypt(byte[] ciphertext, byte[] k, int operationType) throws EncryptionException {
		try {
			byte[] key = makeOffset(k);
			SecretKey secretKey = new SecretKeySpec(key, "AES");

			IvParameterSpec ivParams = new IvParameterSpec(iv);
			cipher.init(operationType, secretKey, ivParams);
			byte[] output = cipher.doFinal(ciphertext);

			return output;
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	private byte[] makeOffset(byte[] k) {
		byte[] result = new byte[cipher.getBlockSize()];
		System.arraycopy(k, 0, result, 0, k.length);

		return result;
	}

	private byte[] toBinaryBytes(long n) {
		return ByteBuffer.allocate(Long.BYTES).putLong(n).array();
	}

}



