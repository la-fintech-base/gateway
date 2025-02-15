package la.com.gateway.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    INIT(0),
    ACTIVE(1),
    INACTIVE(2),
    PENDING_CONFIRM_PHONE(3),
    PENDING_EKYC(4);
    private final Integer value;
    private static final Map<Integer, UserStatusEnum> userStatusMap = new HashMap<>();

    static {
        for (UserStatusEnum userStatusEnum : UserStatusEnum.values()) {
            userStatusMap.put(userStatusEnum.value, userStatusEnum);
        }
    }

    public static UserStatusEnum of(Integer s) {
        return userStatusMap.get(s);
    }
}
