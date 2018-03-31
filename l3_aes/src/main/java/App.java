import com.sun.tools.javac.util.Pair;
import encryption.Encryption;
import utils.EncMode;

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

	private enum AppMode {challenge, encryption_oracle}

	public static void main(String[] args) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {

		getInput();


		if (appMode == AppMode.challenge) {
			challengeMode();
		} else if (appMode == AppMode.encryption_oracle) {
			cipherMessages();
		}
	}

	private static void challengeMode() {
		Scanner input = new Scanner(System.in);
		while (true) {
			String first = input.nextLine();
			if (first == "q") {
				break;
			}
			String second = input.nextLine();

			Pair<String, Integer> b = Encryption.challenge(Arrays.asList(first, second), key, encMode);
			System.out.println(b.fst);
		}
		input.close();
	}

	private static void cipherMessages() {
		System.out.println("Give number of messages to encrypt: ");
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		List<File> files = new ArrayList<>(Collections.nCopies(n, null));

		System.out.println("Give files with messages: ");
		for (int i = 0; i < n; ++i) {
			String filename = scanner.next();
			File file = new File(filename);
			files.set(i, file);
		}
		Encryption.encryptFiles(encMode, files, key);
	}

	private static void getInput() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		keystore = KeyStore.getInstance("jceks");
		password = (String) Files.lines(Paths.get(configFilePath)).toArray()[0];
		String keystoreFilePath = readKeystorePath();
		System.out.println(keystoreFilePath);
		keyId = readKeyId();
		encMode = readEncMode();
		keystore.load(new FileInputStream(keystoreFilePath), password.toCharArray());
		key = keystore.getKey(keyId, password.toCharArray());
		appMode = readAppMode();
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

//src/main/resources/keystore.jceks
//aes_key