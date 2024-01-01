package org.example.otvattendancebotdispatcher.command;

import java.util.List;
import org.example.otvattendancebotdispatcher.service.SendQueueMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class QueueCommandContainer {

    private final List<String> queueCommands;
    private final SendQueueMessageService sendQueueMessageService;

    public QueueCommandContainer(SendQueueMessageService sendQueueMessageService) {
        this.sendQueueMessageService = sendQueueMessageService;
        this.queueCommands = List.of(
            "/register",
            "/top"
        );
    }

    public boolean retrieveCommand(String commandIdentifier, Update update) {
        var isQueueCommand = queueCommands.contains(commandIdentifier);
        if (isQueueCommand) {
            sendQueueMessageService.sendToAttendanceSendQueue(update);
        }

        return isQueueCommand;
    }
}
