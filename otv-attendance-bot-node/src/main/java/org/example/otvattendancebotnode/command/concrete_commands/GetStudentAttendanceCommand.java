package org.example.otvattendancebotnode.command.concrete_commands;

import java.time.format.DateTimeFormatter;
import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.entity.Attendance;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.StudentRepository;
import org.example.otvattendancebotnode.repository.TeacherRepository;

public class GetStudentAttendanceCommand implements Command {

    private final JmsProducer jmsProducer;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public GetStudentAttendanceCommand(JmsProducer jmsProducer, StudentRepository studentRepository,
        TeacherRepository teacherRepository) {
        this.jmsProducer = jmsProducer;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public void execute(AttendanceQueueModel model) {
        var text = model.getText().trim().split(" ");
        var studentPersonnelNumber = Long.valueOf(text[1]);
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
        if (student.getTelegramUserId().equals(model.getTelegramUserId()) ||
            teacherRepository.findByTelegramUserId(model.getTelegramUserId()).isPresent()) {
            StringBuilder result = new StringBuilder();

            var attendances = student.getAttendances();
            if (attendances.isEmpty()) {
                result.append("Студент не посещал занятий");
            } else {
                result.append("Информация о посещениях студента:\n");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

                for (Attendance attendance : attendances) {
                    result.append("Дата: ").append(attendance.getDate().format(formatter))
                        .append(", Предмет: ").append(attendance.getSubject().getTitle())
                        .append(", Посещение: ").append(attendance.isAttendance() ? "Присутствовал" : "Отсутствовал")
                        .append("\n");
                }
            }
            var successModel = new AttendanceQueueModel(
                result.toString(),
                model.getChatId()
            );
            jmsProducer.send(successModel);
        } else {
            var notEnoughRightsModel = new AttendanceQueueModel(
                "Произошла ошибка! Вы не можете смотреть чужие отметки!",
                model.getChatId()
            );
            jmsProducer.send(notEnoughRightsModel);
        }
    }
}
