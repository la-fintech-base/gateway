package la.com.gateway.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessTokenPayload {
    private Long userId;
    private String username;
    private String mobile;
    private String email;
    private String personalEmail;
    private Integer ekycStatus;
    private Integer type;
    private Integer userType;
    private Long expiredTime;
    private String padding;
    private Integer linkedBank;
    private String sessionId;
    private String refUserId;
    private String campus;
    private String platform;
    private Integer status;
}
