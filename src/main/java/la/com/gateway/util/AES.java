package la.com.gateway.util;

import la.com.gateway.common.exception.LaException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
public class AES {

    private AES() {
        throw new IllegalStateException("Utility class");
    }

    public static final int AES_KEY_SIZE = 256;
    public static final int GCM_TAG_LENGTH = 16;

    public static String encrypt(String strToEncrypt, String secret, String ivBase64) {
        try {
            byte[] strToEncryptBytes = strToEncrypt.getBytes(StandardCharsets.UTF_8);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secret.toCharArray(), secret.getBytes(), 65536, AES_KEY_SIZE);
            SecretKey secretKey = factory.generateSecret(spec);

            byte[] iv = decodeIV(ivBase64);

            return Base64.getEncoder().encodeToString(encrypt(strToEncryptBytes, secretKey, iv));
        } catch (Exception e) {
            throw new LaException(e.getMessage());
        }
    }

    public static String decrypt(String strToDecrypt, String secret, String ivBase64) {
        try {
            byte[] strToDecryptBytes = Base64.getDecoder().decode(strToDecrypt);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secret.toCharArray(), secret.getBytes(), 65536, AES_KEY_SIZE);
            SecretKey secretKey = factory.generateSecret(spec);

            byte[] iv = decodeIV(ivBase64);

            return decrypt(strToDecryptBytes, secretKey, iv);
        } catch (Exception e) {
            throw new LaException(e.getMessage());
        }
    }

    public static byte[] encrypt(byte[] plaintext, SecretKey key, byte[] iV) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iV);

        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Encryption
        return cipher.doFinal(plaintext);
    }

    public static String decrypt(byte[] cipherText, SecretKey key, byte[] iV) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iV);

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }

    public static String generateIV(int length) {
        byte[] iV = new byte[length];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iV);
        log.info("IV: " + Arrays.toString(iV));
        String ivBase64 = Base64.getEncoder().encodeToString(iV);
        log.info("ivBase64: " + ivBase64);
        return ivBase64;
    }

    public static byte[] decodeIV(String ivBase64) {
        return Base64.getDecoder().decode(ivBase64);
    }

}
