package org.example.otvattendancebotnode.command.concrete_commands;

import java.util.Optional;
import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.entity.Group;
import org.example.otvattendancebotnode.entity.Student;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.GroupRepository;
import org.example.otvattendancebotnode.repository.StudentRepository;
import org.example.otvattendancebotnode.repository.TeacherRepository;

public class GetGroupAttendanceCommand implements Command {

    private final JmsProducer jmsProducer;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public GetGroupAttendanceCommand(JmsProducer jmsProducer, GroupRepository groupRepository,
        StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.jmsProducer = jmsProducer;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public void execute(AttendanceQueueModel model) {
        var text = model.getText().trim().split(" ");
        var groupName = text[1];
        var telegramUserId = model.getTelegramUserId();

        Optional<Group> groupOptional = groupRepository.findByName(groupName);
        if (groupOptional.isEmpty()) {
            var groupDoesntExistModel = new AttendanceQueueModel(
                "Группа " + groupName + " не существует!",
                model.getChatId()
            );
            jmsProducer.send(groupDoesntExistModel);
            return;
        }

        var group = groupOptional.get();

        Optional<Student> studentOptional = studentRepository.findByTelegramUserId(telegramUserId);
        if (studentOptional.isPresent()) {
            if (!studentOptional.get().getGroup().getName().equals(groupName)) {
                sendNotEnoughRightsModel(model);
                return;
            }
        } else if (teacherRepository.findByTelegramUserId(telegramUserId).isEmpty()) {
            sendNotEnoughRightsModel(model);
            return;
        }

        StringBuilder result = new StringBuilder();

        for (var student : group.getStudentList()) {
            var attendances = student.getAttendances();
            if (attendances.isEmpty()) {
                result.append(
                    "Студент: " + student.getLastName() + " " + student.getFirstName() + " "
                        + student.getPersonnelNumber() + " не посещал занятий\n");
            } else {
                result.append(
                        student.getLastName() + " " + student.getFirstName() + " " + student.getPersonnelNumber() + ":\n")
                    .append("Количество посещенных занятий: " + student.getAttendances().size() + "\n\n");
            }
        }

        var successModel = new AttendanceQueueModel(
            result.toString(),
            model.getChatId()
        );
        jmsProducer.send(successModel);
    }

    private void sendNotEnoughRightsModel(AttendanceQueueModel model) {
        var notEnoughRightsModel = new AttendanceQueueModel(
            "У вас нет прав для просмотра посещаемости чужой группы!",
            model.getChatId()
        );
        jmsProducer.send(notEnoughRightsModel);
    }
}
