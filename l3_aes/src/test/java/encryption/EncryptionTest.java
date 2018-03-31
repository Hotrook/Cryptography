package encryption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.CertificateException;

import static encryption.Encryption.decrypt;
import static encryption.Encryption.encryptCBC;
import static org.junit.Assert.assertEquals;

public class EncryptionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEncryptCBC() throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
		File file = new File("files/file1.txt");
		File decryptedFile = new File(file.getAbsolutePath() + ".dec");
		File encryptedFile = new File(decryptedFile.getAbsolutePath() + ".enc");

		Cipher cipher = Cipher.getInstance(Encryption.CBC);
		encryptCBC(file, cipher, getSecretKey());
		decrypt(decryptedFile, cipher, getSecretKey());

		String first = (String) Files.lines(file.toPath()).toArray()[0];
		String second = (String) Files.lines(encryptedFile.toPath()).toArray()[0];

		assertEquals(first, second);
	}

	@Test
	public void testEncryptCTR() throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
		File file = new File("files/file1.txt");
		File decryptedFile = new File(file.getAbsolutePath() + ".dec");
		File encryptedFile = new File(decryptedFile.getAbsolutePath() + ".enc");

		Cipher cipher = Cipher.getInstance(Encryption.CTR);
		encryptCBC(file, cipher, getSecretKey());
		decrypt(decryptedFile, cipher, getSecretKey());

		String first = (String) Files.lines(file.toPath()).toArray()[0];
		String second = (String) Files.lines(encryptedFile.toPath()).toArray()[0];

		assertEquals(first, second);
	}

	@Test
	public void testEncryptOFB() throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
		File file = new File("files/file1.txt");
		File decryptedFile = new File(file.getAbsolutePath() + ".dec");
		File encryptedFile = new File(decryptedFile.getAbsolutePath() + ".enc");

		Cipher cipher = Cipher.getInstance(Encryption.OFB);
		encryptCBC(file, cipher, getSecretKey());
		decrypt(decryptedFile, cipher, getSecretKey());

		String first = (String) Files.lines(file.toPath()).toArray()[0];
		String second = (String) Files.lines(encryptedFile.toPath()).toArray()[0];

		assertEquals(first, second);
	}

	public SecretKey getSecretKey() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
		String keystoreFilePath = "src/main/resources/keystore.jceks";
		KeyStore keystore = KeyStore.getInstance("jceks");
		keystore.load(new FileInputStream(keystoreFilePath), "krypto".toCharArray());

		Key key = keystore.getKey("aes_key", "krypto".toCharArray());
		SecretKey secretKey = new SecretKeySpec(key.getEncoded(), "AES");
		return secretKey;
	}
}