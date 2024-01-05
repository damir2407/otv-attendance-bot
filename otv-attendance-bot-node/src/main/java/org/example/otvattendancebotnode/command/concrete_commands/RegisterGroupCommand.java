package org.example.otvattendancebotnode.command.concrete_commands;

import org.example.otvattendancebotnode.command.Command;
import org.example.otvattendancebotnode.entity.Group;
import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;
import org.example.otvattendancebotnode.jms.producer.JmsProducer;
import org.example.otvattendancebotnode.repository.GroupRepository;
import org.example.otvattendancebotnode.repository.ModeratorRepository;

public class RegisterGroupCommand implements Command {

    private final JmsProducer jmsProducer;

    private final GroupRepository groupRepository;

    private final ModeratorRepository moderatorRepository;

    public RegisterGroupCommand(JmsProducer jmsProducer, GroupRepository groupRepository,
        ModeratorRepository moderatorRepository) {
        this.jmsProducer = jmsProducer;
        this.groupRepository = groupRepository;
        this.moderatorRepository = moderatorRepository;
    }

    @Override
    public void execute(AttendanceQueueModel model) {
        var text = model.getText().trim().split(" ");
        var telegramUserId = model.getTelegramUserId();
        var groupName = text[1];

        if (moderatorRepository.findByTelegramUserId(telegramUserId).isEmpty()) {
            var notEnoughRightsModel = new AttendanceQueueModel(
                "Недостаточно прав для выполнения данной операции!",
                model.getChatId()
            );
            jmsProducer.send(notEnoughRightsModel);
            return;
        }

        if (groupRepository.findByName(groupName).isPresent()) {
            var groupAlreadyExistModel = new AttendanceQueueModel(
                "Группа " + groupName + " уже существует!",
                model.getChatId()
            );
            jmsProducer.send(groupAlreadyExistModel);
            return;
        }
        var group = new Group();
        group.setName(groupName);
        groupRepository.save(group);

        var successfullyRegisteredModel = new AttendanceQueueModel(
            "Группа успешно зарегистрирована!",
            model.getChatId()
        );
        jmsProducer.send(successfullyRegisteredModel);
    }
}
