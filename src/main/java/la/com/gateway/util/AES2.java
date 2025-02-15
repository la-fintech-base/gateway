package la.com.gateway.util;

import la.com.gateway.common.exception.LaException;
import la.com.gateway.common.util.CommonUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AES2 {

    private AES2() {
        throw new IllegalStateException("Utility class");
    }

    @SuppressWarnings("java:S6432")
    public static String encrypt(String strToEncrypt, SecretKeySpec secretKeySpec, GCMParameterSpec gcmParameterSpec) {
        //encrypt
        try {
            Cipher cipherEncrypt = Cipher.getInstance("AES/GCM/NoPadding");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
            byte[] strToEncryptBytes = strToEncrypt.getBytes(StandardCharsets.UTF_8);
            byte[] cipherText = cipherEncrypt.doFinal(strToEncryptBytes);

            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new LaException(e.getMessage());
        }
    }

    public static String decrypt(String strToDecrypt, SecretKeySpec secretKeySpec, GCMParameterSpec gcmParameterSpec){
        try {
            Cipher cipherDecrypt = Cipher.getInstance("AES/GCM/NoPadding");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
            byte[] strToDecryptBytes = Base64.getDecoder().decode(strToDecrypt);
            byte[] decryptedText = cipherDecrypt.doFinal(strToDecryptBytes);
            return new String(decryptedText);
        }catch (Exception e){
            throw new LaException(e.getMessage());
        }
    }

    public static  <T> T decrypt(String token, Class<T> returnType, SecretKeySpec secretKeySpec, GCMParameterSpec gcmParameterSpec) {
        String json = decrypt(token, secretKeySpec, gcmParameterSpec);
        return CommonUtil.fromJson(json, returnType);
    }


}

