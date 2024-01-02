package org.example.otvattendancebotnode.command.concrete_commands;

import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;

public class StartCommand implements Command {

    private final JmsProducer jmsProducer;

    private final static String START_MESSAGE =
        "Привет, я Attendance Telegram Bot и я помогу тебе быть в курсе последних новостей о посещаемости в университете.\n\n"
            +
            "Моя цель - не только фиксировать твою посещаемость, но и предоставлять полезную информацию. " +
            "Узнай свой рейтинг среди студентов, следи за своей активностью и получай уведомления о своей посещаемости. \n\n"
            +
            "Для начала, отправь мне команду /help, чтобы узнать, как я могу тебе помочь.";


    public StartCommand(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    @Override
    public void execute(AttendanceQueueModel model) {
        var modelToSend = new AttendanceQueueModel(
            START_MESSAGE,
            model.getChatId()
        );
        jmsProducer.send(modelToSend);
    }
}