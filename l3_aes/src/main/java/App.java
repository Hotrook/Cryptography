import com.sun.tools.javac.util.Pair;
import encryption.Encryption;
import encryption.Encryptor;
import utils.EncMode;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;

public class App {

	private static List<String> encModesName = Arrays.asList("OFB", "CTR", "CBC");
	private static String configFilePath = "src/main/resources/password.txt";
	private static String keyId;
	private static String password;
	private static KeyStore keystore;
	private static Key key;
	private static EncMode encMode;
	private static AppMode appMode;

	private enum AppMode {challenge, encryption_oracle, def}

	public static void main(String[] args) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, NoSuchPaddingException {

		getInput();


		if (appMode == AppMode.challenge) {
			challengeMode();
		} else if (appMode == AppMode.encryption_oracle) {
			cipherMessages();
		}
	}

	private static void challengeMode() throws NoSuchAlgorithmException, NoSuchPaddingException {
		Scanner input = new Scanner(System.in);
		Encryptor enc = Encryption.getEncryptor(encMode);
		while (true) {
			String first = input.nextLine();
			if (first == "q") {
				break;
			}
			String second = input.nextLine();

			Pair<String, Integer> b = enc.challenge(Arrays.asList(first, second), key, encMode);
			System.out.println(b.fst);
		}
		input.close();
	}

	private static void cipherMessages() throws NoSuchAlgorithmException, NoSuchPaddingException {
		System.out.println("Give number of messages to encrypt: ");
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		List<File> files = new ArrayList<>(Collections.nCopies(n, null));
		Encryptor enc = Encryption.getEncryptor(encMode);

		System.out.println("Give files with messages: ");
		for (int i = 0; i < n; ++i) {
			String filename = scanner.next();
			File file = new File(filename);
			files.set(i, file);
		}
		enc.encryptFiles(files, key);
	}

	private static void getInput() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		String keystoreFilePath;
		appMode = readAppMode();
		if (appMode == AppMode.def) {
			keystoreFilePath = "src/main/resources/keystore.jceks";
			encMode = EncMode.CBC;
			keyId = "aes_key";
			appMode = AppMode.encryption_oracle;
		} else {
			keystoreFilePath = readKeystorePath();
			keyId = readKeyId();
			encMode = readEncMode();
		}
		keystore = KeyStore.getInstance("jceks");
		password = (String) Files.lines(Paths.get(configFilePath)).toArray()[0];
		keystore.load(new FileInputStream(keystoreFilePath), password.toCharArray());
		key = keystore.getKey(keyId, password.toCharArray());
	}

	private static AppMode readAppMode() {
		System.out.println("Give application mode(challenge, encryption_oracle): ");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();

		return AppMode.valueOf(input);
	}

	private static String readKeyId() {
		System.out.println("Give key identifier: ");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();

		return input;
	}

	private static String readKeystorePath() {
		System.out.println("Give keystore path: ");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();

		return input;
	}

	private static EncMode readEncMode() {
		System.out.println("Choose encryption mode (OFB, CTR, CBC) ): ");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();

		return EncMode.valueOf(input);
	}
}
