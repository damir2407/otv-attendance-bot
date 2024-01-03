package org.example.otvattendancebotnode.command.concrete_commands;

import java.util.Optional;
import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.entity.Teacher;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.ModeratorRepository;
import org.example.otvattendancebotnode.repository.TeacherRepository;

public class RegisterTeacherCommand implements Command {

    private final JmsProducer jmsProducer;
    private final ModeratorRepository moderatorRepository;
    private final TeacherRepository teacherRepository;

    public RegisterTeacherCommand(
        JmsProducer jmsProducer,
        ModeratorRepository moderatorRepository,
        TeacherRepository teacherRepository) {
        this.jmsProducer = jmsProducer;
        this.moderatorRepository = moderatorRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public void execute(AttendanceQueueModel model) {
        var text = model.getText().trim().split(" ");
        var teacherTelegramUserId = Long.valueOf(text[1]);
        var telegramUserId = model.getTelegramUserId();

        if (moderatorRepository.findByTelegramUserId(telegramUserId).isEmpty()) {
            var notEnoughRightsModel = new AttendanceQueueModel(
                "Недостаточно прав для выполнения данной операции!",
                model.getChatId()
            );
            jmsProducer.send(notEnoughRightsModel);
            return;
        }

        Optional<Teacher> teacherOptional = teacherRepository.findByTelegramUserId(teacherTelegramUserId);
        if (teacherOptional.isPresent()) {
            var teacherAlreadyExistModel = new AttendanceQueueModel(
                "Учитель с userId = " + teacherTelegramUserId + " уже зарегистрирован!",
                model.getChatId()
            );
            jmsProducer.send(teacherAlreadyExistModel);
            return;
        }

        var teacher = new Teacher();
        teacher.setTelegramUserId(teacherTelegramUserId);
        teacherRepository.save(teacher);
        var successfullyRegisteredModel = new AttendanceQueueModel(
            "Учитель успешно зарегистрирован!",
            model.getChatId()
        );
        jmsProducer.send(successfullyRegisteredModel);
    }
}
