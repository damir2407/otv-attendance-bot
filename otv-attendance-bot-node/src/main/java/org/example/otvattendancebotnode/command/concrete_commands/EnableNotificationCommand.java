package org.example.otvattendancebotnode.command.concrete_commands;

import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.StudentRepository;

public class EnableNotificationCommand implements Command {

    private final JmsProducer jmsProducer;
    private final StudentRepository studentRepository;

    public EnableNotificationCommand(JmsProducer jmsProducer, StudentRepository studentRepository) {
        this.jmsProducer = jmsProducer;
        this.studentRepository = studentRepository;
    }

    @Override
    public void execute(AttendanceQueueModel model) {
        var telegramUserId = model.getTelegramUserId();

        var studentOptional = studentRepository.findByTelegramUserId(telegramUserId);
        if (studentOptional.isEmpty()) {
            var studentDoesntExistModel = new AttendanceQueueModel(
                "Вы не зарегистрированы как студент!",
                model.getChatId()
            );
            jmsProducer.send(studentDoesntExistModel);
            return;
        }
        var student = studentOptional.get();
        student.setNotificationEnabled(true);
        studentRepository.save(student);

        var successModel = new AttendanceQueueModel(
            "Уведомления успешно включены!",
            model.getChatId()
        );
        jmsProducer.send(successModel);
    }
}
