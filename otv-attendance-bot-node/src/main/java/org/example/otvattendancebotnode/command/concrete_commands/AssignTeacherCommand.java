package org.example.otvattendancebotnode.command.concrete_commands;

import java.util.Optional;
import java.util.regex.Pattern;
import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.entity.Assignment;
import org.example.otvattendancebotnode.entity.Group;
import org.example.otvattendancebotnode.entity.Subject;
import org.example.otvattendancebotnode.entity.Teacher;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.AssignmentRepository;
import org.example.otvattendancebotnode.repository.GroupRepository;
import org.example.otvattendancebotnode.repository.ModeratorRepository;
import org.example.otvattendancebotnode.repository.SubjectRepository;
import org.example.otvattendancebotnode.repository.TeacherRepository;

public class AssignTeacherCommand implements Command {

    private final JmsProducer jmsProducer;

    private final ModeratorRepository moderatorRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final AssignmentRepository assignmentRepository;

    private final static String regex = "/assign_teacher (\\d+)\\s+(\\S+(?:\\s+\\S*)*)\\s+(\\S+)";




    public AssignTeacherCommand(
        JmsProducer jmsProducer,
        ModeratorRepository moderatorRepository, SubjectRepository subjectRepository,
        TeacherRepository teacherRepository, GroupRepository groupRepository,
        AssignmentRepository assignmentRepository) {
        this.jmsProducer = jmsProducer;
        this.moderatorRepository = moderatorRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.groupRepository = groupRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public void execute(AttendanceQueueModel model) {
        var text = model.getText().trim();
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(text);
        var telegramUserId = model.getTelegramUserId();

        if (matcher.find()) {
            var teacherTelegramUserId = Long.valueOf(matcher.group(1));
            var subjectTitle = matcher.group(2);
            var groupName = matcher.group(3);

            if (moderatorRepository.findByTelegramUserId(telegramUserId).isEmpty()) {
                var notEnoughRightsModel = new AttendanceQueueModel(
                    "Недостаточно прав для выполнения данной операции!",
                    model.getChatId()
                );
                jmsProducer.send(notEnoughRightsModel);
                return;
            }

            Optional<Teacher> teacherOptional = teacherRepository.findByTelegramUserId(teacherTelegramUserId);
            if (teacherOptional.isEmpty()) {
                var teacherDoesntExistModel = new AttendanceQueueModel(
                    "Учителя с userId = " + teacherTelegramUserId + " не существует!",
                    model.getChatId()
                );
                jmsProducer.send(teacherDoesntExistModel);
                return;
            }

            Optional<Subject> subjectOptional = subjectRepository.findByTitle(subjectTitle);
            if (subjectOptional.isEmpty()) {
                var subjectDoesntExistModel = new AttendanceQueueModel(
                    "Предмет " + subjectTitle + " не существует!",
                    model.getChatId()
                );
                jmsProducer.send(subjectDoesntExistModel);
                return;
            }

            Optional<Group> groupOptional = groupRepository.findByName(groupName);
            if (groupOptional.isEmpty()) {
                var groupDoesntExistModel = new AttendanceQueueModel(
                    "Группа " + groupName + " не существует!",
                    model.getChatId()
                );
                jmsProducer.send(groupDoesntExistModel);
                return;
            }

            var teacher = teacherOptional.get();
            var subject = subjectOptional.get();
            var group = groupOptional.get();

            var assignment = new Assignment(
                subject,
                teacher,
                group
            );

            assignmentRepository.save(assignment);

            var successfullyRegisteredModel = new AttendanceQueueModel(
                "Учителю успешно назначены права!",
                model.getChatId()
            );
            jmsProducer.send(successfullyRegisteredModel);
        }


    }
}
