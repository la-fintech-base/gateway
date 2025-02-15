package la.com.gateway.util;

import la.com.gateway.common.exception.LaException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class BlowFish {
    private BlowFish() {
        throw new IllegalStateException("Utility class");
    }

    @SuppressWarnings({"java:S5542", "java:S5547", "java:S5542"})
    public static String encrypt(String input, SecretKeySpec keySpec) {
        try {
            Cipher cipherEncrypt = Cipher.getInstance("Blowfish");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(cipherEncrypt.doFinal(input.getBytes()));
        } catch (Exception ex) {
            throw new LaException(ex.getMessage());
        }
    }

    @SuppressWarnings({"java:S5542", "java:S5547", "java:S5542"})
    public static String decrypt(String input, SecretKeySpec keySpec) {
        try {
            Cipher cipherDecrypt = Cipher.getInstance("Blowfish");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, keySpec);

            return new String(cipherDecrypt.doFinal((Base64.getDecoder().decode(input))));
        } catch (Exception ex) {
            throw new LaException(ex.getMessage());
        }
    }
}
