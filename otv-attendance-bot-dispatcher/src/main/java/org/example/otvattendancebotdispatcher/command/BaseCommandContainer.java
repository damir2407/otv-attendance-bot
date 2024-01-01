package org.example.otvattendancebotdispatcher.command;

import static org.example.otvattendancebotdispatcher.command.CommandName.HELP;
import static org.example.otvattendancebotdispatcher.command.CommandName.START;

import com.google.common.collect.ImmutableMap;
import org.example.otvattendancebotdispatcher.command.concrete_commands.HelpCommand;
import org.example.otvattendancebotdispatcher.command.concrete_commands.StartCommand;
import org.example.otvattendancebotdispatcher.command.concrete_commands.UnknownCommand;
import org.example.otvattendancebotdispatcher.service.SendBotMessageService;

public class BaseCommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;

    public BaseCommandContainer(SendBotMessageService sendBotMessageService) {
        commandMap = ImmutableMap.<String, Command>builder()
            .put(START.getCommandName(), new StartCommand(sendBotMessageService))
            .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
            .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
