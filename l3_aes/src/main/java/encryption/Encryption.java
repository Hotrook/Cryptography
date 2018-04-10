package encryption;

import utils.EncMode;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Encryption {

	public static void encryptFiles(EncMode encMode, List<File> files, Key key) {
		if (encMode == EncMode.CBC) {
			try {
				Encryptor enc = new EncryptorCBC();
				enc.encryptFiles(files, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (encMode == EncMode.CTR) {
			try {
				Encryptor enc = new EncryptorCTR();
				enc.encryptFiles(files, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (encMode == EncMode.OFB) {
			try {
				Encryptor enc = new EncryptorOFB();
				enc.encryptFiles(files, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Encryptor getEncryptor(EncMode encMode) throws NoSuchPaddingException, NoSuchAlgorithmException {
		if (encMode == EncMode.CBC) {
			return new EncryptorCBC();
		} else if (encMode == EncMode.CTR) {
			return new EncryptorCTR();
		} else if (encMode == EncMode.OFB) {
			return new EncryptorOFB();
		}
		return null;
	}
}
