package org.example.otvattendancebotnode.command.concrete_commands;


import static org.example.otvattendancebotnode.command.CommandName.ASSIGN_TEACHER;
import static org.example.otvattendancebotnode.command.CommandName.DISABLE_NOTIFICATION;
import static org.example.otvattendancebotnode.command.CommandName.ENABLE_NOTIFICATION;
import static org.example.otvattendancebotnode.command.CommandName.GET_ATTENDANCE;
import static org.example.otvattendancebotnode.command.CommandName.GET_GROUP_ATTENDANCE;
import static org.example.otvattendancebotnode.command.CommandName.HELP;
import static org.example.otvattendancebotnode.command.CommandName.MARK;
import static org.example.otvattendancebotnode.command.CommandName.REGISTER;
import static org.example.otvattendancebotnode.command.CommandName.REGISTER_GROUP;
import static org.example.otvattendancebotnode.command.CommandName.REGISTER_SUBJECT;
import static org.example.otvattendancebotnode.command.CommandName.REGISTER_TEACHER;
import static org.example.otvattendancebotnode.command.CommandName.START;

import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;

public class HelpCommand implements Command {

    private final JmsProducer jmsProducer;
    private static final String HELP_MESSAGE = String.format("✨<b>Доступные команды</b>✨\n\n"
            + "%s - начать работу со мной\n"
            + "%s - получить помощь в работе со мной\n"
            + "%s [номер группы] [фамилия] [имя] [табельный номер] - зарегистрироваться студентом\n"
            + "%s [userId] - зарегистрировать учителя (модератору)\n"
            + "%s [название группы] - зарегистрировать группу (модератору)\n"
            + "%s [название предмета] - зарегистрировать предмет (модератору)\n"
            + "%s [userId] [название предмета] [название группы] - назначить учителя на предмет в группе (модератору)\n"
            + "%s [название предмета] [табельный номер] - поставить отметку посещения студенту\n"
            + "%s [табельный номер] - получить информацию о посещаемости студента\n"
            + "%s [название группы] - получить информацию о посещаемости группы\n"
            + "%s - включить уведомления о топе студентов (7:00 МСК)\n"
            + "%s - выключить уведомления о топе студентов (7:00 МСК)\n",
        START.getCommandName(), HELP.getCommandName(), REGISTER.getCommandName(),
        REGISTER_TEACHER.getCommandName(), REGISTER_GROUP.getCommandName(),
        REGISTER_SUBJECT.getCommandName(), ASSIGN_TEACHER.getCommandName(),
        MARK.getCommandName(), GET_ATTENDANCE.getCommandName(),
        GET_GROUP_ATTENDANCE.getCommandName(), ENABLE_NOTIFICATION.getCommandName(),
        DISABLE_NOTIFICATION.getCommandName());

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
