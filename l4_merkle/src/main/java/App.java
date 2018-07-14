import Encryption.AESEncryptor;
import Encryption.EncryptionException;
import Encryption.Encryptor;
import Puzzler.Puzzler;
import Transmitter.Transmitter;

import java.util.Date;

public class App {

	public static void main(String[] args) throws EncryptionException {
		Encryptor enc = new AESEncryptor();

		Puzzler generator = new Puzzler(16, enc);
		Puzzler receiver = new Puzzler();

		Transmitter transmitter = new Transmitter(generator, receiver);

		generator.setTransmitter(transmitter);
		receiver.setTransmitter(transmitter);

		System.out.println((new Date()).toString());
		generator.run();
		System.out.println((new Date()).toString());

	}
}
