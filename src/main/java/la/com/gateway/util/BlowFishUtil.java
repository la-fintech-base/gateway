package la.com.gateway.util;

import jakarta.annotation.PostConstruct;
import la.com.gateway.common.exception.LaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
@Slf4j
@Service
public class BlowFishUtil {

    @Value("${ewallet.card.cipher.secret.key}")
    private String secretKey;
    private SecretKeySpec keySpec;

    @PostConstruct
    public void init(){
        try {
            keySpec = new SecretKeySpec(secretKey.getBytes(), "Blowfish");
        } catch (Exception ex) {
            throw new LaException(ex.getMessage());
        }
    }

    public String decrypt(String input){
        return BlowFish.decrypt(input, keySpec);
    }

    public String encrypt(String input){
        return BlowFish.encrypt(input, keySpec);
    }

}
