package encryption;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class EncryptorCTR extends Encryptor {

	public static String cipherType = "AES/CTR/PKCS5Padding";

	public EncryptorCTR() throws NoSuchAlgorithmException, NoSuchPaddingException {
		super(cipherType);
	}

}
