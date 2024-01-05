package org.example.otvattendancebotnode.command.concrete_commands;

import java.util.Optional;
import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.entity.Group;
import org.example.otvattendancebotnode.entity.Student;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.GroupRepository;
import org.example.otvattendancebotnode.repository.StudentRepository;

public class RegisterCommand implements Command {

    private final JmsProducer jmsProducer;

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public RegisterCommand(
        JmsProducer jmsProducer,
        StudentRepository studentRepository,
        GroupRepository groupRepository
    ) {
        this.jmsProducer = jmsProducer;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public void execute(AttendanceQueueModel model) {

        var text = model.getText().trim().split(" ");
        var groupName = text[1];
        var studentLastName = text[2];
        var studentFirstName = text[3];
        var studentPersonnelNumber = Long.valueOf(text[4]);
        var telegramUserId = model.getTelegramUserId();

        Optional<Group> group = groupRepository.findByName(groupName);
        if (group.isEmpty()) {
            var groupDoesntExistModel = new AttendanceQueueModel(
                "Группа " + groupName + " не существует!",
                model.getChatId()
            );
            jmsProducer.send(groupDoesntExistModel);
            return;
        }

        if (studentRepository.findByTelegramUserId(telegramUserId).isPresent()) {
            var studentAlreadyExistModel = new AttendanceQueueModel(
                "Пользователь с userId = " + telegramUserId + " уже существует!",
                model.getChatId()
            );
            jmsProducer.send(studentAlreadyExistModel);
            return;
        }

        var student = new Student(
            studentFirstName,
            studentLastName,
            model.getTelegramName(),
            model.getChatId(),
            telegramUserId,
            false,
            group.get(),
            studentPersonnelNumber
        );

        studentRepository.save(student);
        var studentCreatedModel = new AttendanceQueueModel(
            "Пользователь успешно зарегистрирован!",
            model.getChatId()
        );
        jmsProducer.send(studentCreatedModel);
    }
}
