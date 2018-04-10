package distinguisher;

import com.sun.tools.javac.util.Pair;
import encryption.Encryptor;
import encryption.EncryptorCBC;
import utils.EncMode;

import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CPA {

	public static int iv = 1;
	private static String configFilePath = "src/main/resources/password.txt";
	private static String password;
	private static String keyId = "aes_key";
	private static String keystyorePath = "src/main/resources/keystore.jceks";
	private static KeyStore keystore;
	private static Key key;
	private static EncMode encMode;

	private final static String MESSSAGE_1 = "1234567890ABCDEF1";
	private final static String MESSSAGE_2 = "ABCDEF12345678902";
	private final static String MESSSAGE_3 = "ABC1234567890DEF";

	public static void main(String[] args) throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchPaddingException {

		readKey();
		List<String> ciphertexts = new ArrayList();
		Encryptor enc = new EncryptorCBC();

		while (ciphertexts.size() < 2) {
			String xored1 = xor(MESSSAGE_1, iv);
			String xored2 = xor(MESSSAGE_2, iv);

			Pair<String, Integer> result = enc.challenge(Arrays.asList(xored1, xored2), key, EncMode.CBC);

			System.out.println(result.fst);
			if (!ciphertexts.contains(result.fst)) {
				ciphertexts.add(result.fst);
			}
			iv++;
		}

		String xored1 = xor(MESSSAGE_1, iv);
		String xored3 = xor(MESSSAGE_3, iv);

		Pair<String, Integer> result = enc.challenge(Arrays.asList(xored1, xored3), key, EncMode.CBC);

		int b = 0;
		if (!ciphertexts.contains(result.fst)) {
			b = 1;
		}

		if (b == result.snd) {
			System.out.println("You're a winner.");
		} else {
			System.out.println("You're a loser.");
		}

	}

	private static String xor(String messsage1, int iv) {
		byte[] message = messsage1.getBytes();
		byte[] byteIv = ByteBuffer.allocate(16).putInt(iv).array();
		byte[] result = new byte[message.length];

		for (int i = 0; i < message.length; ++i) {
			result[i] = (byte) (message[i] ^ byteIv[i]);
		}

		return new String(result);
	}

	private static void readKey() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
		keystore = KeyStore.getInstance("jceks");
		password = (String) Files.lines(Paths.get(configFilePath)).toArray()[0];
		keystore.load(new FileInputStream(keystyorePath), password.toCharArray());
		key = keystore.getKey(keyId, password.toCharArray());
	}
}
