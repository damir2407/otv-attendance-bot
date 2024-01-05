package org.example.otvattendancebotnode.command.concrete_commands;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;
import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.entity.Attendance;
import org.example.otvattendancebotnode.entity.Teacher;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.AttendanceRepository;
import org.example.otvattendancebotnode.repository.StudentRepository;
import org.example.otvattendancebotnode.repository.TeacherRepository;

public class AttendanceCommand implements Command {

    private final JmsProducer jmsProducer;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    private final static String regex = "/mark\\s+(.+?)\\s+(\\d+)";

    public AttendanceCommand(JmsProducer jmsProducer, TeacherRepository teacherRepository,
        StudentRepository studentRepository, AttendanceRepository attendanceRepository) {
        this.jmsProducer = jmsProducer;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public void execute(AttendanceQueueModel model) {
        var text = model.getText().trim();
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(text);
        var teacherTelegramUserId = model.getTelegramUserId();

        if (matcher.find()) {
            var subjectTitle = matcher.group(1).trim();
            var studentPersonnelNumber = Long.valueOf(matcher.group(2).trim());

            Optional<Teacher> teacherOptional = teacherRepository.findByTelegramUserId(teacherTelegramUserId);
            if (teacherOptional.isEmpty()) {
                var teacherDoesntExistsModel = new AttendanceQueueModel(
                    "У вас недостаточно прав для выполнения этой операции!",
                    model.getChatId()
                );
                jmsProducer.send(teacherDoesntExistsModel);
                return;
            }

            var studentOptional = studentRepository.findByPersonnelNumber(studentPersonnelNumber);
            if (studentOptional.isEmpty()) {
                var studentDoesntExistModel = new AttendanceQueueModel(
                    "Студент с табельным номером = " + studentPersonnelNumber + " не существует!",
                    model.getChatId()
                );
                jmsProducer.send(studentDoesntExistModel);
                return;
            }
            var student = studentOptional.get();
            var studentsGroup = student.getGroup();

            var teacher = teacherOptional.get();
            var assignments = teacher.getAssignments();

            for (var assignment : assignments) {
                var subject = assignment.getSubject();
                var group = assignment.getGroup();

                if (group.getName().equals(studentsGroup.getName()) &&
                    subject.getTitle().equals(subjectTitle)) {

                    var attendance = new Attendance(
                        subject,
                        student,
                        true,
                        LocalDateTime.now()
                    );

                    attendanceRepository.save(attendance);

                    var successfullyAttendantModel = new AttendanceQueueModel(
                        "Отметка студенту " + student.getLastName() + " " + student.getFirstName()
                            + " успешно выставлена!",
                        model.getChatId()
                    );
                    jmsProducer.send(successfullyAttendantModel);
                    return;
                }
            }

            var notEnoughRightsModel = new AttendanceQueueModel(
                "Произошла ошибка! Возможно у вас нет прав для выставления отметки",
                model.getChatId()
            );
            jmsProducer.send(notEnoughRightsModel);
        }
    }
}
