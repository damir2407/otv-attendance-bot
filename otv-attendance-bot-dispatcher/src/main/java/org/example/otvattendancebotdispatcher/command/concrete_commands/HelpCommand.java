package org.example.otvattendancebotdispatcher.command.concrete_commands;

import static org.example.otvattendancebotdispatcher.command.CommandName.HELP;
import static org.example.otvattendancebotdispatcher.command.CommandName.START;

import org.example.otvattendancebotdispatcher.command.Command;
import org.example.otvattendancebotdispatcher.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public static final String HELP_MESSAGE = String.format("✨<b>Доступные команды</b>✨\n\n"

            + "%s - начать работу со мной\n"
            + "%s - получить помощь в работе со мной\n",
        START.getCommandName(), HELP.getCommandName());

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), HELP_MESSAGE);
    }
}
