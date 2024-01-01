package org.example.otvattendancebotdispatcher.service;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otvattendancebotdispatcher.configuration.jms.JmsQueues;
import org.example.otvattendancebotdispatcher.configuration.jms.model.AttendanceSendQueueModel;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendQueueMessageServiceImpl implements SendQueueMessageService {

    private final JmsQueues jmsQueues;
    private final SQSConnectionFactory sqsConnectionFactory;
    private final ObjectMapper objectMapper;

    @Override
    public void sendToAttendanceSendQueue(Update update) {
        try {
            var message = update.getMessage();
            var model = new AttendanceSendQueueModel(
                message.getText(),
                message.getChatId()
            );

            String jsonObject = objectMapper.writeValueAsString(model);

            send(jmsQueues.getAttendanceSendQueue(), jsonObject);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    private void send(String queueName, String jsonObject) {
        try {
            SQSConnection connection = sqsConnectionFactory.createConnection();

            AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

            if (!client.queueExists(queueName)) {
                client.createQueue(queueName);
            }

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Queue queue = session.createQueue(queueName);

            MessageProducer producer = session.createProducer(queue);

            Message message = session.createTextMessage(jsonObject);
            producer.send(message);

            session.close();
            connection.close();
        } catch (JMSException e) {
            log.error(e.getMessage());
        }
    }
}
