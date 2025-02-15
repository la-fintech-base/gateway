package la.com.gateway.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum UserEkycStatus {
    INIT(0),
    ACTIVE(1),
    INACTIVE(2);
    private final Integer value;
    private static final Map<Integer, UserEkycStatus> map = new HashMap<>();

    static {
        for (UserEkycStatus userEkycStatus : UserEkycStatus.values()) {
            map.put(userEkycStatus.value, userEkycStatus);
        }
    }

    public static UserEkycStatus of(Integer s) {
        return map.get(s);
    }
}
