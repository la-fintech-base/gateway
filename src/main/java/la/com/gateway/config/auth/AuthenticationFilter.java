package la.com.gateway.config.auth;

import la.com.gateway.common.constant.ErrorEnum;
import la.com.gateway.common.constant.HttpHeader;
import la.com.gateway.common.constant.UserEkycStatus;
import la.com.gateway.common.constant.UserStatusEnum;
import la.com.gateway.common.exception.LaException;
import la.com.gateway.common.model.AccessTokenPayload;
import la.com.gateway.service.AccessTokenService;
import la.com.gateway.util.AESUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@SuppressWarnings("java:S1598")
@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter {

    @Value("${internal.api.key}")
    private String internalApiKey;

    private final AccessTokenService accessTokenService;
    private final AESUtil aesUtil;
//    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (isPublicApi(path)) {
            request = exchange.getRequest()
                    .mutate().headers(httpHeaders -> httpHeaders.add(HttpHeader.INTERNAL_API_KEY,
                            internalApiKey))
                    .build();
            return chain.filter(exchange.mutate().request(request).build());
        }

        if (isInternalApi(path)) {
            throw new LaException(ErrorEnum.ACCESS_DENIED);
        }

        String accessToken = request.getHeaders().getFirst("Authorization");
        if (StringUtils.isBlank(accessToken)) {
            throw new LaException(ErrorEnum.INVALID_ACCESS_TOKEN);
        }

        // Check hết hạn token
        AccessTokenPayload payload = accessTokenService.getExternalTokenPayload(accessToken);
        if (payload.getExpiredTime() == null
                || payload.getExpiredTime() < System.currentTimeMillis()) {
            throw new LaException(ErrorEnum.SESSION_EXPIRED);
        }

        String accessTokenHash = aesUtil.hash(accessToken);
//        String sessionId = this.redisTemplate.opsForValue()
//                .get(RedisKey.SESSION_PREFIX + payload.getUserId());
//        if (sessionId == null || !sessionId.equals(payload.getSessionId())) {
//            throw new LaException(ErrorEnum.ACCOUNT_LOGGED_ANOTHER_DEVICE);
//        }

//        Boolean isTokenExist = this.redisTemplate.hasKey(
//                RedisKey.ACCESS_TOKENS_PREFIX + payload.getUserId() + "_" + accessTokenHash);
//        if (Boolean.FALSE.equals(isTokenExist)) {
//            throw new LaException(ErrorEnum.SESSION_EXPIRED);
//        }

        if (UserStatusEnum.INIT.equals(UserStatusEnum.of(payload.getStatus()))
                && !isEkycApi(path) && !isConfirmPhoneNumber(path) && !isChangePasswordInit(path)) {
            throw new LaException(ErrorEnum.USER_INIT);
        }

        if (UserStatusEnum.PENDING_CONFIRM_PHONE.equals(UserStatusEnum.of(payload.getStatus()))
                && !isEkycApi(path) && !isConfirmPhoneNumber(path) && !isChangePasswordInit(path)) {
            throw new LaException(ErrorEnum.USER_CONFIRM_PHONE);
        }

        if (UserStatusEnum.PENDING_EKYC.equals(UserStatusEnum.of(payload.getStatus()))
                && !UserEkycStatus.ACTIVE.equals(UserEkycStatus.of(payload.getEkycStatus()))
                && !isEkycApi(path) && !isConfirmPhoneNumber(path) && !isChangePasswordInit(path)) {
            throw new LaException(ErrorEnum.USER_NOT_EKYC);
        }

        String internalAccessToken = accessTokenService.generateInternalToken(payload);
        exchange.getAttributes().put("userIdField", payload.getUserId().toString());
        exchange.getAttributes()
                .put("campus", payload.getCampus() != null ? payload.getCampus() : "");
        exchange.getAttributes().put("userType", payload.getUserType().toString());
        ServerWebExchange newExchange = updateHeader(exchange, internalAccessToken);
        return chain.filter(newExchange);
    }

    private boolean isConfirmPhoneNumber(String path) {
        return path.contains("/profile/confirm-phone-number")
                || path.contains("/profile/phone-number")
                || path.contains("/profile/phone-number/verify-otp");
    }

    private boolean isEkycApi(String path) {
        return path.contains("/ekyc/recognition-id") || path.contains("/ekyc/check-face");
    }

    private boolean isChangePasswordInit(String path) {
        return path.contains("/profile/change-password-init");
    }

    private boolean isPublicApi(String path) {
        return path.contains("/public/");
    }

    private boolean isInternalApi(String path) {
        return path.contains("/internal/") || path.contains("/admin/");
    }

    private ServerWebExchange updateHeader(ServerWebExchange exchange, String internalAccessToken) {
        ServerHttpRequest request = exchange.getRequest()
                .mutate().headers(httpHeaders -> {
                    httpHeaders.add(HttpHeader.INTERNAL_ACCESS_TOKEN, internalAccessToken);
                    httpHeaders.add(HttpHeader.INTERNAL_API_KEY, internalApiKey);
                })
                .build();
        return exchange.mutate().request(request).build();
    }
}
