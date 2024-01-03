package org.example.otvattendancebotdispatcher.command;

import java.util.List;
import org.example.otvattendancebotdispatcher.service.jms.SendQueueMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CommandContainer {

    private final List<String> commands;
    private final SendQueueMessageService sendQueueMessageService;

    public CommandContainer(SendQueueMessageService sendQueueMessageService) {
        this.sendQueueMessageService = sendQueueMessageService;
        this.commands = List.of(
            CommandName.HELP.getCommandName(),
            CommandName.START.getCommandName(),
            CommandName.REGISTER.getCommandName()
        );
    }

    public boolean retrieveCommand(String commandIdentifier, Update update) {
        var isCommandExists = commands.contains(commandIdentifier);
        if (isCommandExists) {
            sendQueueMessageService.sendToAttendanceSendQueue(update);
        }
        return isCommandExists;
    }
}
