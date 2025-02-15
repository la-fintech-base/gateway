package la.com.gateway.config.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import net.logstash.logback.composite.JsonWritingUtils;
import net.logstash.logback.composite.loggingevent.MessageJsonProvider;

import java.io.IOException;

public class MaskingMessageProvider extends MessageJsonProvider {

    MaskRule maskRule = new MaskRule();

    @Override
    public void writeTo(JsonGenerator generator, ILoggingEvent event) throws IOException {
        JsonWritingUtils.writeStringField(generator, getFieldName(),maskRule.apply(event.getFormattedMessage()));
    }
}
