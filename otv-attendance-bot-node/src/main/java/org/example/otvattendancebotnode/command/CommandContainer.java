package org.example.otvattendancebotnode.command;

import static org.example.otvattendancebotnode.command.CommandName.HELP;
import static org.example.otvattendancebotnode.command.CommandName.REGISTER;
import static org.example.otvattendancebotnode.command.CommandName.START;

import java.util.Map;
import org.example.otvattendancebotnode.command.concrete_commands.HelpCommand;
import org.example.otvattendancebotnode.command.concrete_commands.RegisterCommand;
import org.example.otvattendancebotnode.command.concrete_commands.StartCommand;
import org.example.otvattendancebotnode.command.concrete_commands.UnknownCommand;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.GroupRepository;
import org.example.otvattendancebotnode.repository.StudentRepository;
import org.springframework.stereotype.Component;

@Component
public class CommandContainer {

    private final Map<String, Command> commandMap;
    private final Command unknownCommand;

    public CommandContainer(
        JmsProducer jmsProducer,
        GroupRepository groupRepository,
        StudentRepository studentRepository
    ) {
        this.commandMap = Map.of(
            HELP.getCommandName(), new HelpCommand(jmsProducer),
            START.getCommandName(), new StartCommand(jmsProducer),
            REGISTER.getCommandName(), new RegisterCommand(jmsProducer, studentRepository, groupRepository)
        );
        unknownCommand = new UnknownCommand(jmsProducer);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }

}
