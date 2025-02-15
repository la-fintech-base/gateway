package la.com.gateway.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    private Integer httpStatus;
    private String errorCode;
    private String message;
    private String traceId;
    private Boolean success = false;
}
