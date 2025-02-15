package la.com.gateway.service;

import la.com.gateway.common.model.AccessTokenPayload;
import org.springframework.stereotype.Service;

@Service
public interface AccessTokenService {
    AccessTokenPayload getExternalTokenPayload(String externalToken);
    String generateInternalToken(AccessTokenPayload payload);
}
