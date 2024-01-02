package org.example.otvattendancebotnode.jms.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JmsProducer {

    private final ObjectMapper objectMapper;
    @Qualifier("otvDispatcherTemplate")
    private final JmsTemplate jmsTemplate;

    public void send(AttendanceQueueModel model) {
        try {
            String jsonObject = objectMapper.writeValueAsString(model);
            jmsTemplate.convertAndSend(jsonObject);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

}
