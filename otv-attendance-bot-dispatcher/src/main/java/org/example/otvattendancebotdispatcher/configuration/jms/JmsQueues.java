package org.example.otvattendancebotdispatcher.configuration.jms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jms.queue-names")
public class JmsQueues {

    private String attendanceSendQueue;
}
