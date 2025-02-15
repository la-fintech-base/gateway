package la.com.gateway.config.log;

import com.github.skjolber.jsonfilter.core.DefaultJsonLogFilterBuilder;
import com.github.skjolber.jsonfilter.JsonFilter;

public class MaskRule {
    JsonFilter filter = DefaultJsonLogFilterBuilder.createInstance()
            .withMaxStringLength(2048)
            .withAnonymize("$.bankAccountNo", "$.username", "$.password", "$.idCardNo",
                    "$.number", "$.cardNumber", "$.accessToken", "$.refreshToken", "$.otp", "$.sharedKey")
            .withAnonymize("$.*.bankAccountNo", "$.*.username", "$.*.password",
                    "$.*.idCardNo", "$.*.number", "$.*.cardNumber", "$.*.accessToken", "$.*.refreshToken", "$.*.otp",
                    "$.*.sharedKey")
            .withMaxPathMatches(5)
            .withMaxSize(128 * 1024)
            .build();

    MaskRule() {
    }

    public String apply(String input) {
        return maskMessage(input);
    }

    private String maskMessage(String message) {
        return filter.process(message);
    }
}
