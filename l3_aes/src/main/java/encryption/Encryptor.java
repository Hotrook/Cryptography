package encryption;

import com.sun.tools.javac.util.Pair;
import org.apache.commons.codec.binary.Base64;
import utils.EncMode;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class Encryptor {

	public static String AES = "AES";
	protected EncMode encMode;
	protected Cipher cipher;

	public Encryptor(String cipherType) throws NoSuchPaddingException, NoSuchAlgorithmException {
		System.out.println(cipherType);
		this.cipher = Cipher.getInstance(cipherType);
	}

	public void encryptFiles(List<File> files, Key key) {
		SecretKey secretKey = new SecretKeySpec(key.getEncoded(), AES);
		try {
			encryptFiles(files, secretKey);
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
	}

	protected void encryptFiles(List<File> files, SecretKey secretKey) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
		for (File file : files) {
			byte[] iv = generateIv();
			byte[] message = Files.readAllBytes(file.toPath());
			byte[] encrypted = encrypt(message, secretKey);
			writeOutputToFile(encrypted, iv, file.getAbsoluteFile());
		}
	}

	public byte[] encrypt(byte[] message, SecretKey secretKey) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
		byte[] iv = generateIv();
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		byte[] output = cipher.doFinal(message);

		return output;
	}

	protected void decryptFiles(List<File> files, SecretKey secretKey) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
		for (File file : files) {
			byte[] input = Files.readAllBytes(Paths.get(file.getPath()));
			byte[] ciphertext = Arrays.copyOfRange(input, 0, input.length - 24);
			byte[] iv = Arrays.copyOfRange(input, input.length - 24, input.length - 1);
			byte[] encrypted = decrypt(ciphertext, iv, file, secretKey);

			writeOutputToFile(encrypted, file.getAbsoluteFile());
		}
	}

	public byte[] decrypt(byte[] ciphertext, byte[] iv, File file, SecretKey secretKey) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		IvParameterSpec ivParams = new IvParameterSpec(Base64.decodeBase64(iv));
		System.out.println(ivParams.getIV().length);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);
		byte[] output = cipher.doFinal(Base64.decodeBase64(ciphertext));

		return output;
	}

	protected byte[] generateIv() {
		byte[] iv = new byte[cipher.getBlockSize()];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);

		return iv;
	}

	public Pair<String, Integer> challenge(List<String> strings, Key key, EncMode encMode) {
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
		String filename = absoluteFile + ".dec";
		File file = new File(filename);
		FileOutputStream outputStream = new FileOutputStream(file);

		outputStream.write((Base64.encodeBase64String(output)).getBytes());
		outputStream.write((Base64.encodeBase64String(iv)).getBytes());
		outputStream.close();
	}

	private static void writeOutputToFile(byte[] output, File absoluteFile) throws IOException {
		String filename = absoluteFile + ".enc";
		File file = new File(filename);
		FileOutputStream outputStream = new FileOutputStream(file);

		outputStream.write(output);
		outputStream.close();
	}

}
