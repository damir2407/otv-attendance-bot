package org.example.otvattendancebotnode.schedule;

import java.util.Collections;
import java.util.stream.Collectors;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.GroupRepository;
import org.example.otvattendancebotnode.util.StudentComparator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TopNotification {

    private final JmsProducer jmsProducer;

    private final GroupRepository groupRepository;

    public TopNotification(JmsProducer jmsProducer, GroupRepository groupRepository) {
        this.jmsProducer = jmsProducer;
        this.groupRepository = groupRepository;
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void notifyStudents() {
        var groups = groupRepository.findAll();

        for (var group : groups) {
            StringBuilder result = new StringBuilder();
            var students = group.getStudentList();
            var sortedStudents = students.stream().sorted(new StudentComparator()).collect(Collectors.toList());
            Collections.reverse(sortedStudents);

            if (sortedStudents.size() >= 1) {
                var topOneStudent = sortedStudents.get(0);
                result.append("Топ студентов по посещаемости в группе " + group.getName() + ":\n")
                    .append("1 место:\n" + topOneStudent.getLastName() + " " + topOneStudent.getFirstName() + " "
                        + topOneStudent.getPersonnelNumber() + " " + topOneStudent.getAttendances().size()
                        + " посещений!\n\n");

                if (sortedStudents.size() >= 2) {
                    var topSecondStudent = sortedStudents.get(1);
                    result.append(
                        "2 место: \n" + topSecondStudent.getLastName() + " " + topSecondStudent.getFirstName() + " "
                            + topSecondStudent.getPersonnelNumber() + " " + topSecondStudent.getAttendances().size()
                            + " посещений!\n\n");

                    if (sortedStudents.size() >= 3) {
                        var topThirdStudent = sortedStudents.get(2);
                        result.append(
                            "3 место: \n" + topThirdStudent.getLastName() + " " + topThirdStudent.getFirstName() + " "
                                + topThirdStudent.getPersonnelNumber() + " " + topThirdStudent.getAttendances().size()
                                + " посещений!\n\n");
                    }
                }
            }

            if (!result.toString().isEmpty()) {
                for (var student : students) {
                    if (student.isNotificationEnabled()) {
                        var topNotifyModel = new AttendanceQueueModel(
                            result.toString(),
                            student.getTelegramChatId()
                        );
                        jmsProducer.send(topNotifyModel);

                    }
                }
            }

        }
    }


}
