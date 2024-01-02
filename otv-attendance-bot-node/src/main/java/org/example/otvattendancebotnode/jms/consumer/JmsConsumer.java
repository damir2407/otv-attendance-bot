package org.example.otvattendancebotnode.jms.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otvattendancebotnode.command.CommandContainer;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JmsConsumer {

    private final ObjectMapper objectMapper;
    private final CommandContainer commandContainer;

    @JmsListener(destination = "${jms.queues.otv-node.name}")
    public void acceptMessage(String requestJSON) {
        log.info("Прочитано сообщение: " + requestJSON);
        try {
            AttendanceQueueModel model = objectMapper.readValue(requestJSON, AttendanceQueueModel.class);
            String commandIdentifier = model.getText().split(" ")[0].toLowerCase();

            commandContainer.retrieveCommand(commandIdentifier).execute(model);
        } catch (JsonProcessingException e) {
            log.error("Произошла ошибка во время парсинга сообщения!");
        }
    }

}
