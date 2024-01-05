package org.example.otvattendancebotdispatcher.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.otvattendancebotdispatcher.service.jms.SendQueueMessageService;
import org.example.otvattendancebotdispatcher.validate.AlwaysTrueValidator;
import org.example.otvattendancebotdispatcher.validate.AssignTeacherCommandValidator;
import org.example.otvattendancebotdispatcher.validate.GetGroupAttendanceCommandValidator;
import org.example.otvattendancebotdispatcher.validate.GetStudentAttendanceCommandValidator;
import org.example.otvattendancebotdispatcher.validate.MarkCommandValidator;
import org.example.otvattendancebotdispatcher.validate.RegisterCommandValidator;
import org.example.otvattendancebotdispatcher.validate.RegisterGroupCommandValidator;
import org.example.otvattendancebotdispatcher.validate.RegisterSubjectCommandValidator;
import org.example.otvattendancebotdispatcher.validate.RegisterTeacherCommandValidator;
import org.example.otvattendancebotdispatcher.validate.Validate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CommandContainer {

    private final Map<String, Validate> commands;
    private final SendQueueMessageService sendQueueMessageService;

    public CommandContainer(SendQueueMessageService sendQueueMessageService) {
        this.sendQueueMessageService = sendQueueMessageService;
        this.commands = new HashMap<>();
        commands.put(CommandName.HELP.getCommandName(), new AlwaysTrueValidator());
        commands.put(CommandName.START.getCommandName(), new AlwaysTrueValidator());
        commands.put(CommandName.REGISTER.getCommandName(), new RegisterCommandValidator());
        commands.put(CommandName.REGISTER_TEACHER.getCommandName(), new RegisterTeacherCommandValidator());
        commands.put(CommandName.REGISTER_GROUP.getCommandName(), new RegisterGroupCommandValidator());
        commands.put(CommandName.REGISTER_SUBJECT.getCommandName(), new RegisterSubjectCommandValidator());
        commands.put(CommandName.ASSIGN_TEACHER.getCommandName(), new AssignTeacherCommandValidator());
        commands.put(CommandName.MARK.getCommandName(), new MarkCommandValidator());
        commands.put(CommandName.GET_ATTENDANCE.getCommandName(), new GetStudentAttendanceCommandValidator());
        commands.put(CommandName.GET_GROUP_ATTENDANCE.getCommandName(), new GetGroupAttendanceCommandValidator());
        commands.put(CommandName.ENABLE_NOTIFICATION.getCommandName(), new AlwaysTrueValidator());
        commands.put(CommandName.DISABLE_NOTIFICATION.getCommandName(), new AlwaysTrueValidator());
    }

    public boolean retrieveCommand(String text, Update update) {
        var commandName = text.split(" ")[0].toLowerCase();
        Validate validator = commands.get(commandName);

        if (validator != null) {
            var validateResult = validator.validateCommand(text);
            if (!validateResult) {
                return false;
            }
            sendQueueMessageService.sendToAttendanceSendQueue(update);
            return true;
        }
        return false;
    }
}
