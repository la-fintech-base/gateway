package la.com.gateway.common.constant;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    INVALID_INPUT_COMMON(400, "invalid_input", "%s"),
    INVALID_INPUT(400, "invalid_input", "Dữ liệu đầu vào không hợp lệ"),
    DUPLICATE_ERROR(400, "duplicate_error", "%s"),
    INTERNAL_SERVER_ERROR(500, "internal_server_error", "Đã có lỗi xảy ra. Xin vui lòng thử lại sau hoặc liên hệ quản trị viên để được hỗ trợ."),
    ACCESS_DENIED(403, "access_denied", "Bạn không có quyền truy cập tài nguyên này"),
    SESSION_EXPIRED(401, "session_expired", "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại."),
    USER_NOT_EKYC(401, "user_not_ekyc", "Người dùng chưa xác minh danh tính."),
    ACCOUNT_LOGGED_ANOTHER_DEVICE(401, "account_logged_another_device", "Tài khoản của bạn đã được đăng nhập trên thiết bị khác, vui lòng kiểm tra lại"),
    INVALID_ACCESS_TOKEN(401, "invalid_access_token", "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại."),
    INVALID_APP_VERSION(400, "invalid_app_version", "Vui lòng cập nhật ứng dụng lên phiên bản mới nhất để sử dụng chức năng này"),
    USER_INIT(401, "user_init", "Người dùng chưa có quyền truy cập tài nguyên này do chưa đổi mật khẩu"),
    USER_CONFIRM_PHONE(401, "user_confirm_phone", "Người dùng xác thực số điện thoại");

    private final Integer httpStatus;
    private final String code;
    private final String message;

    ErrorEnum(Integer httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}
