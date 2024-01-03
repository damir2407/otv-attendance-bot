package org.example.otvattendancebotnode.command.concrete_commands;

import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.entity.Group;
import org.example.otvattendancebotnode.entity.Subject;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.ModeratorRepository;
import org.example.otvattendancebotnode.repository.SubjectRepository;

public class RegisterSubjectCommand implements Command {

    private final JmsProducer jmsProducer;

    private final SubjectRepository subjectRepository;

    private final ModeratorRepository moderatorRepository;

    public RegisterSubjectCommand(JmsProducer jmsProducer, SubjectRepository subjectRepository,
        ModeratorRepository moderatorRepository) {
        this.jmsProducer = jmsProducer;
        this.subjectRepository = subjectRepository;
        this.moderatorRepository = moderatorRepository;
    }

    @Override
    public void execute(AttendanceQueueModel model) {
        var text = model.getText().trim();
        var telegramUserId = model.getTelegramUserId();
        var subjectTitle = text.substring(text.indexOf(" ") + 1);

        if (moderatorRepository.findByTelegramUserId(telegramUserId).isEmpty()) {
            var notEnoughRightsModel = new AttendanceQueueModel(
                "Недостаточно прав для выполнения данной операции!",
                model.getChatId()
            );
            jmsProducer.send(notEnoughRightsModel);
            return;
        }

        if (subjectRepository.findByTitle(subjectTitle).isPresent()) {
            var groupAlreadyExistModel = new AttendanceQueueModel(
                "Предмет " + subjectTitle + " уже существует!",
                model.getChatId()
            );
            jmsProducer.send(groupAlreadyExistModel);
            return;
        }
        var subject = new Subject();
        subject.setTitle(subjectTitle);
        subjectRepository.save(subject);

        var successfullyRegisteredModel = new AttendanceQueueModel(
            "Предмет успешно зарегистрирован!",
            model.getChatId()
        );
        jmsProducer.send(successfullyRegisteredModel);

    }
}
