package encryption;

import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

public class EncryptorCBC extends Encryptor {

	public static String cipherType = "AES/CBC/PKCS5Padding";
	public int ivCounter = 1;

	public EncryptorCBC() throws NoSuchAlgorithmException, NoSuchPaddingException {
		super(cipherType);
	}

	@Override
	protected byte[] generateIv() {
		byte[] iv = ByteBuffer.allocate(cipher.getBlockSize()).putInt(ivCounter).array();
		ivCounter++;

		return iv;
	}

}
