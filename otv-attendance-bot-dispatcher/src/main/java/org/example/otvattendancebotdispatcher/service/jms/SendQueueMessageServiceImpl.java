package org.example.otvattendancebotdispatcher.service.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otvattendancebotdispatcher.service.jms.model.AttendanceQueueModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendQueueMessageServiceImpl implements SendQueueMessageService {

    private final ObjectMapper objectMapper;
    @Qualifier("otvNodeTemplate")
    private final JmsTemplate jmsTemplate;


    @Override
    public void sendToAttendanceSendQueue(Update update) {
        try {
            var message = update.getMessage();
            var model = new AttendanceQueueModel(
                message.getText(),
                message.getChatId()
            );

            String jsonObject = objectMapper.writeValueAsString(model);

            jmsTemplate.convertAndSend(jsonObject);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
