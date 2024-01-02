package org.example.otvattendancebotnode.command;

import static org.example.otvattendancebotnode.command.CommandName.HELP;
import static org.example.otvattendancebotnode.command.CommandName.START;

import java.util.Map;
import org.example.otvattendancebotnode.command.concrete_commands.HelpCommand;
import org.example.otvattendancebotnode.command.concrete_commands.StartCommand;
import org.example.otvattendancebotnode.command.concrete_commands.UnknownCommand;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.springframework.stereotype.Component;

@Component
public class CommandContainer {

    private final Map<String, Command> commandMap;
    private final Command unknownCommand;

    public CommandContainer(JmsProducer jmsProducer) {
        this.commandMap = Map.of(
            HELP.getCommandName(), new HelpCommand(jmsProducer),
            START.getCommandName(), new StartCommand(jmsProducer)
        );
        unknownCommand = new UnknownCommand(jmsProducer);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }

}
