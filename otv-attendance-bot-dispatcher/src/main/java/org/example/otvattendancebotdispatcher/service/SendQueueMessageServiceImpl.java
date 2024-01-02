package org.example.otvattendancebotdispatcher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otvattendancebotdispatcher.configuration.jms.JmsQueues;
import org.example.otvattendancebotdispatcher.configuration.jms.model.AttendanceSendQueueModel;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendQueueMessageServiceImpl implements SendQueueMessageService {

    private final JmsQueues jmsQueues;
    private final ObjectMapper objectMapper;
    private final JmsTemplate defaultJmsTemplate;

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
        defaultJmsTemplate.convertAndSend(queueName, jsonObject);
    }
}
