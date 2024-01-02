package org.example.otvattendancebotnode.configuration.jms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jms")
public class JmsProperties {

    private String accessKey;
    private String secretKey;
}
