package la.com.gateway.common.constant;

public class HttpHeader {

    private HttpHeader() {
        throw new IllegalStateException("Utility class");
    }

    public static final String CLIENT_PLATFORM = "x-client-platform";
    public static final String CLIENT_PLATFORM_VERSION = "x-client-platform-version";
    public static final String CLIENT_DEVICE_ID = "x-client-device-id";
    public static final String INTERNAL_API_KEY = "x-internal-api-key";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String X_CLIENT_APP_TYPE = "x-client-app-type";
    public static final String X_CLIENT_APP_VERSION = "x-client-app-version";
    public static final String INTERNAL_ACCESS_TOKEN = "internal-access-token";
}
