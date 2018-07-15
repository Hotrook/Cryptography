package com.frost.cpaaes;

import com.frost.cpaaes.encryption.Encryption;
import com.frost.cpaaes.encryption.EncryptionException;
import com.frost.cpaaes.encryption.Encryptor;
import com.frost.cpaaes.utils.EncMode;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyStore;
import java.util.*;

public class App {

	private static String CONFIG_FILE_PATH = "src/main/resources/password.txt";
	public static final String KEYSTORE_PATH = "src/main/resources/keystore.jceks";
	private static String keyId;
	private static String password;
	private static KeyStore keystore;
	private static Key key;
	private static EncMode encMode;
	private static AppMode appMode;

	public static final Logger log = LoggerFactory.getLogger(App.class);

	private enum AppMode {challenge, encryption_oracle, def}

	public static void main(String[] args) {
		try {
			getInput();

			if (appMode == AppMode.challenge) {
				challengeMode();
			} else if (appMode == AppMode.encryption_oracle) {
				cipherMessages();
			}
		} catch (KeyException e) {
			log.info("There was an error connected with reading a key. App is terminating...", e);
		} catch (IOException e) {
			log.info("There was an error connected with standard input/output. App is terminating...", e);
		} catch (EncryptionException e) {
			log.info("There was an error connected with encryption. App is terminating...", e);
		}
	}

	private static void challengeMode() throws EncryptionException {
		Scanner input = new Scanner(System.in);
		Encryptor enc = Encryption.getEncryptor(encMode);
		boolean shouldStop = false;

		while (!shouldStop) {
			System.out.println("Type a first message(in order to stop type 'q'):");
			String first = input.nextLine();
			log.info("First read message: {}|", first);

			if (!first.equals("q")) {
				System.out.println("Type a second message:");
				String second = input.nextLine();
				log.info("Second read message: {}|", second);


				Pair<String, Integer> result = enc.challenge(Arrays.asList(first, second), key);
				System.out.println("Encrypted message: " + result.getKey());
			} else {
				shouldStop = true;
			}
		}
		input.close();
	}

	private static void cipherMessages() throws EncryptionException {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Give number of messages to encrypt: ");
		int n = scanner.nextInt();
		List<File> files = new ArrayList<>(Collections.nCopies(n, null));
		Encryptor enc = Encryption.getEncryptor(encMode);

		System.out.println("Give files with messages: ");
		for (int i = 0; i < n; ++i) {
			files.set(i, new File(scanner.next()));
		}

		enc.encryptFiles(files, key);

		scanner.close();
	}

	private static void getInput() throws KeyException, IOException {
		Scanner scanner = new Scanner(System.in);

		appMode = readAppMode(scanner);

		if (appMode == AppMode.def) {
			keyId = "aes_key";
			encMode = EncMode.CBC;
			appMode = AppMode.encryption_oracle;
		} else {
			keyId = readKeyId(scanner);
			encMode = readEncMode(scanner);
		}

		try {
			keystore = KeyStore.getInstance("jceks");
			password = (String) Files.lines(Paths.get(CONFIG_FILE_PATH)).toArray()[0];
			keystore.load(new FileInputStream(KEYSTORE_PATH), password.toCharArray());
			key = keystore.getKey(keyId, password.toCharArray());
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new KeyException("There was a problem accessing a key", e);
		}
	}

	private static AppMode readAppMode(Scanner scanner) {
		System.out.println("Give application mode(challenge, encryption_oracle): ");
		String input = scanner.next();
		log.info("Read app mode: {}", input);

		return AppMode.valueOf(input);
	}

	private static String readKeyId(Scanner scanner) {
		System.out.println("Give key identifier: ");
		String input = scanner.next();
		log.info("Read key id: {}", input);

		return input;
	}

	private static EncMode readEncMode(Scanner scanner) {
		System.out.println("Choose encryption mode (OFB, CTR, CBC) ): ");
		String input = scanner.next();
		log.info("Read encryption mode: input");

		return EncMode.valueOf(input);
	}
}
