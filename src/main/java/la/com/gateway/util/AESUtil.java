package la.com.gateway.util;

import jakarta.annotation.PostConstruct;
import la.com.gateway.common.exception.LaException;
import la.com.gateway.common.model.AccessTokenPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@SuppressWarnings("uncheck")
@Slf4j
@Service
public class AESUtil {
    public static final int AES_KEY_SIZE = 256;
    public static final int GCM_TAG_LENGTH = 16;
    @Value("${ewallet.secret.key}")
    private String secretKey;
    @Value("${ewallet.secret.iv}")
    private String secretIV;
    @Value("${hash.salt:+WW=2c*8eW#da*5#&8M#}")
    private String hashSalt;
    private SecretKeySpec keySpec;
    private GCMParameterSpec gcmParameterSpec;

    @PostConstruct
    public void init() {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), secretKey.getBytes(), 65536, AES_KEY_SIZE);
            byte[] iv = Base64.getDecoder().decode(secretIV);
            keySpec = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        } catch (Exception ex) {
            throw new LaException(ex.getMessage());
        }
    }

    public AccessTokenPayload decrypt(String token, Class<AccessTokenPayload> returnType) {
        return AES2.decrypt(token, returnType, keySpec, gcmParameterSpec);
    }

    public String hash(String input) {
        input = input + hashSalt;
        return DigestUtils.sha256Hex(input);
    }


    @SuppressWarnings({"java:S106", "java:S2068", "java:S4507"})
    public static void main(String[] args) {
        try {
            // Tạo secretIv ngẫu nhiên
            byte[] iv = new byte[16]; // 16 bytes cho AES-CBC
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            String secretIv = Base64.getEncoder().encodeToString(iv);

            // Tạo secretKey ngẫu nhiên
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256); // Độ dài khóa 256-bit
            SecretKey secretKey = keyGenerator.generateKey();
            String secretKeyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            // Tạo hashSalt ngẫu nhiên
            byte[] salt = new byte[16]; // Độ dài tùy chọn
            random.nextBytes(salt);
            String hashSalt = Base64.getEncoder().encodeToString(salt);

            // Tạo card.cipher.secretKey từ mật khẩu
            String password = "Fptupay@2023";
            byte[] passwordBytes = password.getBytes();
            byte[] hashedSalt = hashSalt.getBytes();

            // Kết hợp passwordBytes và hashedSalt để tạo key
            byte[] combinedBytes = new byte[passwordBytes.length + hashedSalt.length];
            System.arraycopy(passwordBytes, 0, combinedBytes, 0, passwordBytes.length);
            System.arraycopy(hashedSalt, 0, combinedBytes, passwordBytes.length, hashedSalt.length);
            SecretKey cardCipherSecretKey = new SecretKeySpec(combinedBytes, "AES");
            String cardCipherSecretKeyString = Base64.getEncoder().encodeToString(cardCipherSecretKey.getEncoded());

            // In ra kết quả
            System.out.println("secretIv: " + secretIv);
            System.out.println("secretKey: " + secretKeyString);
            System.out.println("hashSalt: " + hashSalt);
            System.out.println("card.cipher.secretKey: " + cardCipherSecretKeyString);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
