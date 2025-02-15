package la.com.gateway.service.impl;

import la.com.gateway.common.constant.ErrorEnum;
import la.com.gateway.common.exception.LaException;
import la.com.gateway.common.model.AccessTokenPayload;
import la.com.gateway.common.util.CommonUtil;
import la.com.gateway.util.AESUtil;
import la.com.gateway.util.BlowFishUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
public class AccessTokenServiceImpl {
    private final AESUtil aesUtil;
    private final BlowFishUtil blowFishUtil;


    public AccessTokenServiceImpl(AESUtil aesUtil, BlowFishUtil blowFishUtil) {
        this.aesUtil = aesUtil;
        this.blowFishUtil = blowFishUtil;
    }

    public AccessTokenPayload getExternalTokenPayload(String externalToken) {
        try {
            return aesUtil.decrypt(externalToken, AccessTokenPayload.class);
        } catch (Exception ex) {
            log.error("Token invalid");
            throw new LaException(ErrorEnum.INVALID_ACCESS_TOKEN);
        }
    }

    public String generateInternalToken(AccessTokenPayload payload) {
        String payloadJson = CommonUtil.toJson(payload);
        return blowFishUtil.encrypt(payloadJson);
    }

}
