package org.example.otvattendancebotnode.command.concrete_commands;

import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.StudentRepository;

public class DisableNotificationCommand implements Command {

    private final JmsProducer jmsProducer;
    private final StudentRepository studentRepository;

    public DisableNotificationCommand(JmsProducer jmsProducer, StudentRepository studentRepository) {
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
        student.setNotificationEnabled(false);
        studentRepository.save(student);

        var successModel = new AttendanceQueueModel(
            "Уведомления успешно выключены!",
            model.getChatId()
        );
        jmsProducer.send(successModel);
    }
}
