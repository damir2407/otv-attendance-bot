package org.example.otvattendancebotnode.configuration.jms;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import javax.jms.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

@Configuration
@EnableJms
@RequiredArgsConstructor
public class JmsConfiguration {

    private final JmsProperties jmsProperties;

    @Bean(name = "SQSConsumerConnectionFactory")
    public SQSConnectionFactory SQSConsumerConnectionFactory(
        @Value("${jms.queues.otv-node.service-endpoint}") String serviceEndpoint,
        @Value("${jms.queues.otv-node.signing-region}") String signingRegion
    ) {
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
                .withRegion(signingRegion)
                .withEndpointConfiguration(new EndpointConfiguration(
                    serviceEndpoint,
                    signingRegion)
                )
        );
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
        @Qualifier("SQSConsumerConnectionFactory") SQSConnectionFactory sqsConsumerConnectionFactory
    ) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(sqsConsumerConnectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        return factory;
    }

    @Bean(name = "SQSProducerConnectionFactory")
    public SQSConnectionFactory SQSProducerConnectionFactory(
        @Value("${jms.queues.otv-dispatcher.service-endpoint}") String serviceEndpoint,
        @Value("${jms.queues.otv-dispatcher.signing-region}") String signingRegion
    ) {
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
                .withRegion(signingRegion)
                .withEndpointConfiguration(new EndpointConfiguration(
                        serviceEndpoint,
                        signingRegion
                    )
                )
        );
    }

    @Bean
    @Qualifier("otvDispatcherTemplate")
    public JmsTemplate otvDispatcherTemplate(
        @Value("${jms.queues.otv-dispatcher.name}") String queueName,
        @Qualifier("SQSProducerConnectionFactory") SQSConnectionFactory sqsProducerConnectionFactory
    ) {
        JmsTemplate jmsTemplate = new JmsTemplate(sqsProducerConnectionFactory);
        jmsTemplate.setDefaultDestinationName(queueName);
        return jmsTemplate;
    }
}
