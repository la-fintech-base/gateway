package la.com.gateway.config.auth.webfilter;

import io.micrometer.common.util.StringUtils;
import la.com.gateway.common.constant.ErrorEnum;
import la.com.gateway.common.dto.LaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class AuthFilter implements WebFilter {

    @Value("${token.prometheus}")
    private String tokenPrometheus;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getURI().getPath() != null && exchange.getRequest().getURI()
                .getPath().contains("actuator/prometheus")
                && (CollectionUtils.isEmpty(exchange.getRequest().getHeaders().get("Authorization"))
                ||
                StringUtils.isBlank(exchange.getRequest().getHeaders().get("Authorization").get(0))
                ||
                !exchange.getRequest().getHeaders().get("Authorization").get(0)
                        .equals("Bearer " + tokenPrometheus))) {
            throw new LaResponse(ErrorEnum.INVALID_ACCESS_TOKEN);
        }
        return chain.filter(exchange);
    }
}
