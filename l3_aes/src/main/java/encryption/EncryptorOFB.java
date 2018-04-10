package encryption;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class EncryptorOFB extends Encryptor {

	public static String cipherType = "AES/OFB/PKCS5Padding";

	public EncryptorOFB() throws NoSuchPaddingException, NoSuchAlgorithmException {
		super(cipherType);
	}
}
