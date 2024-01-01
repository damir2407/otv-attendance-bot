package org.example.otvattendancebotdispatcher.configuration.jms;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JmsConfiguration {

    private final JmsProperties jmsProperties;

    @Bean
    public SQSConnectionFactory SQSConnectionFactory() {
        return new SQSConnectionFactory(
            new ProviderConfiguration(),

            AmazonSQSClientBuilder
                .standard()
                .withRegion(jmsProperties.getSigningRegion())
                .withEndpointConfiguration(new EndpointConfiguration(
                    jmsProperties.getServiceEndpoint(),
                    jmsProperties.getSigningRegion()))
        );
    }

}
