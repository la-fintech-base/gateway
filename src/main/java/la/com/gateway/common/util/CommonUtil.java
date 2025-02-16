package la.com.gateway.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import la.com.gateway.common.exception.LaException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;

@Slf4j
public class CommonUtil {

    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String UNKNOWN = "unknown";

    private CommonUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static ServerHttpRequest getCurrentHttpRequest() {
        //TODO: làm sau nhé
        return null; // WebFlux không có RequestContextHolder, cần truyền request vào phương thức khi gọi
    }

    public static String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new LaException(e.getMessage());
        }
    }

    public static <T> T fromJson(String json, Class<T> returnType) {
        try {
            return new ObjectMapper().readValue(json, returnType);
        } catch (JsonProcessingException e) {
            throw new LaException(e.getMessage());
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        try {
            return new ObjectMapper().readValue(json, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new LaException(e.getMessage());
        }
    }

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getClientIp(ServerRequest request) {
        String ipAddress = request.headers().firstHeader("X-Forwarded-For");

        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.headers().firstHeader("Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.headers().firstHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            InetSocketAddress remoteAddress = request.exchange().getRequest().getRemoteAddress();
            if (remoteAddress != null) {
                ipAddress = remoteAddress.getAddress().getHostAddress();
                if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
                    try {
                        InetAddress inetAddress = InetAddress.getLocalHost();
                        ipAddress = inetAddress.getHostAddress();
                    } catch (UnknownHostException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }

        if (!StringUtils.isEmpty(ipAddress) && ipAddress.length() > 15 && ipAddress.contains(",")) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }

    public static HttpHeaders extractHeaders(ServerRequest request) {
        HttpHeaders headers = new HttpHeaders();
        request.headers().asHttpHeaders().forEach(headers::addAll);

        headers.addIfAbsent("correlationId", MDC.get("correlationId"));

        // khi gọi qua domain thì nginx căn cứ vào host để forward => luôn forward vào service đầu tiên
        headers.remove("host");

        return headers;
    }
}
