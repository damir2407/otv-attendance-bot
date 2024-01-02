package org.example.otvattendancebotnode.command.concrete_commands;


import static org.example.otvattendancebotnode.command.CommandName.HELP;
import static org.example.otvattendancebotnode.command.CommandName.START;

import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;

public class HelpCommand implements Command {

    private final JmsProducer jmsProducer;
    private static final String HELP_MESSAGE = String.format("✨<b>Доступные команды</b>✨\n\n"

            + "%s - начать работу со мной\n"
            + "%s - получить помощь в работе со мной\n",
        START.getCommandName(), HELP.getCommandName());

    public HelpCommand(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }


    @Override
    public void execute(AttendanceQueueModel model) {
        var modelToSend = new AttendanceQueueModel(
            HELP_MESSAGE,
            model.getChatId()
        );
        jmsProducer.send(modelToSend);
    }
}
