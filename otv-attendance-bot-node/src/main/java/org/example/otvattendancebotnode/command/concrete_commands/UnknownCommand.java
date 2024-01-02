package org.example.otvattendancebotnode.command.concrete_commands;

import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;

public class UnknownCommand implements Command {

    private static final String UNKNOWN_MESSAGE = "Не понимаю вас \uD83D\uDE1F. Напишите /help чтобы узнать список доступных команд.";

    private final JmsProducer jmsProducer;

    public UnknownCommand(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    @Override
    public void execute(AttendanceQueueModel model) {
        var modelToSend = new AttendanceQueueModel(
            UNKNOWN_MESSAGE,
            model.getChatId()
        );
        jmsProducer.send(modelToSend);
    }
}