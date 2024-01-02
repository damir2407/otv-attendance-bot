package org.example.otvattendancebotdispatcher.configuration.jms;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
@RequiredArgsConstructor
public class JmsConfiguration {

    private final JmsProperties jmsProperties;

    @Bean
    public SQSConnectionFactory SQSConnectionFactory() {
        return new SQSConnectionFactory(
            new ProviderConfiguration(),

            AmazonSQSClientBuilder
                .standard()
                .withCredentials(
                    new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(
                            jmsProperties.getAccessKey(),
                            jmsProperties.getSecretKey()
                        )
                    )
                )
                .withRegion(jmsProperties.getSigningRegion())
                .withEndpointConfiguration(new EndpointConfiguration(
                    jmsProperties.getServiceEndpoint(),
                    jmsProperties.getSigningRegion()))
        );
    }

    @Bean
    public JmsTemplate defaultJmsTemplate() {
        return new JmsTemplate(SQSConnectionFactory());
    }
}
