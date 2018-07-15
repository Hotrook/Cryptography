package com.frost.cpaaes.encryption;

import com.frost.cpaaes.utils.EncMode;

public class Encryption {

	public static Encryptor getEncryptor(EncMode encMode) throws EncryptionException {
		if (encMode == EncMode.CBC) {
			return new EncryptorCBC();
		} else if (encMode == EncMode.CTR) {
			return new EncryptorCTR();
		} else if (encMode == EncMode.OFB) {
			return new EncryptorOFB();
		}
		throw new EncryptionException("Unrecognised encryption mode.");
	}

}
