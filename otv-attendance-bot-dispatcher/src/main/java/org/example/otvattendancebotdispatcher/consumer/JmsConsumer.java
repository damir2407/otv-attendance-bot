package org.example.otvattendancebotdispatcher.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otvattendancebotdispatcher.service.SendBotMessageService;
import org.example.otvattendancebotdispatcher.service.jms.model.AttendanceQueueModel;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JmsConsumer {

    private final ObjectMapper objectMapper;
    private final SendBotMessageService sendBotMessageService;

    @JmsListener(destination = "${jms.queues.otv-dispatcher.name}")
    public void acceptMessage(String requestJSON) {
        log.info("Прочитано сообщение: " + requestJSON);
        try {
            AttendanceQueueModel model = objectMapper.readValue(requestJSON, AttendanceQueueModel.class);
            sendBotMessageService.sendMessage(
                model.getChatId(),
                model.getText()
            );
        } catch (JsonProcessingException e) {
            log.error("Произошла ошибка во время парсинга сообщения!");
        }
    }

}