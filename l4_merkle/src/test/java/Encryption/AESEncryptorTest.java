package Encryption;

import org.junit.Assert;
import org.junit.Test;

public class AESEncryptorTest {

	public static final byte[] ORIGINAL_MESSAGE = "Example message".getBytes();

	@Test
	public void When_DecryptAndEncrypt_Then_OriginalMessageExpected() throws EncryptionException {
		Encryptor enc = new AESEncryptor();
		byte[] key = enc.generateKey(24);

		byte[] cipherText = enc.encrypt(ORIGINAL_MESSAGE, key);
		byte[] actualResult = enc.decrypt(cipherText, key);

		Assert.assertArrayEquals(ORIGINAL_MESSAGE, actualResult);
	}

}