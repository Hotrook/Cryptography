package encryption;

import com.sun.tools.javac.util.Pair;
import org.apache.commons.codec.binary.Base64;
import utils.EncMode;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Encryption {

	public final static String CBC = "AES/CBC/PKCS5Padding";
	public final static String OFB = "AES/OFB/PKCS5Padding";
	public final static String CTR = "AES/CTR/PKCS5Padding";
	public final static String AES = "AES";
	private static int CBC_IV = 1;

	public static void encryptFiles(EncMode encMode, List<File> files, Key key) {
		SecretKey secretKey = new SecretKeySpec(key.getEncoded(), AES);
		if (encMode == EncMode.CBC) {
			try {
				encryptCBCFiles(files, secretKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (encMode == EncMode.CTR) {
			try {
				encryptCTRFiles(files, secretKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (encMode == EncMode.OFB) {
			try {
				encryptOFBFiles(files, secretKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void encryptOFBFiles(List<File> files, SecretKey secretKey) throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
		Cipher cipher = Cipher.getInstance(OFB);
		for (File file : files) {
			encryptOFB(file, cipher, secretKey);
		}
	}

	private static void encryptOFB(File file, Cipher cipher, SecretKey secretKey) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
		byte[] iv = generateIV(cipher);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		byte[] output = cipher.doFinal(Files.readAllBytes(file.toPath()));

		writeOutputToFile(output, iv, file.getAbsoluteFile());
	}

	private static void encryptCTRFiles(List<File> files, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {
		Cipher cipher = Cipher.getInstance(CTR);
		for (File file : files) {
			encryptCTR(file, cipher, secretKey);
		}
	}

	private static void encryptCTR(File file, Cipher cipher, SecretKey secretKey) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
		byte[] iv = generateIV(cipher);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		byte[] output = cipher.doFinal(Files.readAllBytes(file.toPath()));

		writeOutputToFile(output, iv, file.getAbsoluteFile());

	}

	public static void encryptCBCFiles(List<File> files, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IOException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance(CBC);
		for (File file : files) {
			encryptCBC(file, cipher, secretKey);
		}
	}

	public static byte[] encryptCBC(File file, Cipher cipher, SecretKey secretKey) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
		byte[] iv = ByteBuffer.allocate(cipher.getBlockSize()).putInt(CBC_IV).array();
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		byte[] output = cipher.doFinal(Files.readAllBytes(file.toPath()));

		CBC_IV++;

		writeOutputToFile(output, iv, file.getAbsoluteFile());

		return output;
	}

	public static void writeOutputToFile(byte[] output, byte[] iv, File absoluteFile) throws IOException {
		String filename = absoluteFile + ".dec";
		File file = new File(filename);
		FileOutputStream outputStream = new FileOutputStream(file);

		outputStream.write((Base64.encodeBase64String(output)).getBytes());
		outputStream.write((Base64.encodeBase64String(iv)).getBytes());
		outputStream.close();
	}

	public static void decrypt(File file, Cipher cipher, SecretKey secretKey) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		FileInputStream inputStream = new FileInputStream(file);
		byte[] input = new byte[(int) file.length()];
		inputStream.read(input);
		byte[] ciphertext = Arrays.copyOfRange(input, 0, input.length - 24);
		byte[] iv = Arrays.copyOfRange(input, input.length - 24, input.length - 1);

		IvParameterSpec ivParams = new IvParameterSpec(Base64.decodeBase64(iv));
		System.out.println(ivParams.getIV().length);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);
		byte[] output = cipher.doFinal(Base64.decodeBase64(ciphertext));

		writeOutputToFile(output, file.getAbsoluteFile());
	}

	private static void writeOutputToFile(byte[] output, File absoluteFile) throws IOException {
		String filename = absoluteFile + ".enc";
		File file = new File(filename);
		FileOutputStream outputStream = new FileOutputStream(file);

		outputStream.write(output);
		outputStream.close();
	}

	private static byte[] generateIV(Cipher cipher) {
		byte[] iv = new byte[cipher.getBlockSize()];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);
		return iv;
	}


	public static Pair<String, Integer> challenge(List<String> strings, Key key, EncMode encMode) {
		Random generator = new Random();
		int b = generator.nextInt(2);
		SecretKey secretKey = new SecretKeySpec(key.getEncoded(), AES);
		byte[] output = null;

		if (encMode == EncMode.CBC) {
			try {
				output = encryptCBC(strings.get(b), secretKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (encMode == EncMode.OFB) {
			try {
				output = encryptOFB(strings.get(b), secretKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (encMode == EncMode.CTR) {
			try {
				output = encryptCTR(strings.get(b), secretKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String result = Base64.encodeBase64String(output).toString();
		return new Pair<String, Integer>(result, b);
	}

	private static byte[] encryptCTR(String s, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance(OFB);
		byte[] iv = ByteBuffer.allocate(cipher.getBlockSize()).putInt(CBC_IV).array();
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		byte[] output = cipher.doFinal(s.getBytes());

		CBC_IV++;

		return output;
	}

	private static byte[] encryptOFB(String s, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance(OFB);
		byte[] iv = ByteBuffer.allocate(cipher.getBlockSize()).putInt(CBC_IV).array();
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		byte[] output = cipher.doFinal(s.getBytes());


		return output;
	}

	private static byte[] encryptCBC(String s, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance(CBC);
		byte[] iv = ByteBuffer.allocate(cipher.getBlockSize()).putInt(CBC_IV).array();
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		byte[] output = cipher.doFinal(s.getBytes());

		CBC_IV++;

		return output;
	}
}
