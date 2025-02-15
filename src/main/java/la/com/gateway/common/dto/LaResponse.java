package la.com.gateway.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import la.com.gateway.common.constant.ErrorEnum;
import la.com.gateway.common.util.LogUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LaResponse extends RuntimeException {
    private final boolean isInternal;
    private final Integer httpStatus;
    private final String errorCode;
    private final String message;
    private final String errorType;
    private final transient Object data;

    public LaResponse(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.isInternal = false;
        this.httpStatus = errorResponse.getHttpStatus();
        this.errorCode = errorResponse.getErrorCode();
        this.message = errorResponse.getMessage();
        this.data = null;
        this.errorType = null;
    }

    public LaResponse(ErrorEnum errorEnum, String... args) {
        super(String.format(errorEnum.getMessage(), (Object[]) args));
        this.isInternal = true;
        this.httpStatus = errorEnum.getHttpStatus();
        this.errorCode = errorEnum.getCode();
        this.message = String.format(errorEnum.getMessage(), (Object[]) args);
        this.data = null;
        this.errorType = null;
    }

    public LaResponse(int httpStatus, String errorCode, String message) {
        super(message);
        this.isInternal = false;
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
        this.data = null;
        this.errorType = null;
    }

    public LaResponse(String msg) {
        this(ErrorEnum.INVALID_INPUT_COMMON, msg);
    }

    public boolean isErrorEnum(ErrorEnum errorEnum) {
        return errorEnum.getCode().equals(this.errorCode);
    }

    public ErrorResponse convertToErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpStatus(this.httpStatus);
        errorResponse.setErrorCode(this.errorCode);
        errorResponse.setMessage(this.getMessage());
        errorResponse.setTraceId(LogUtil.getCorrelationId());
        return errorResponse;
    }
}