package org.example.otvattendancebotdispatcher.command.concrete_commands;

import org.example.otvattendancebotdispatcher.command.Command;
import org.example.otvattendancebotdispatcher.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public final static String START_MESSAGE =
        "Привет, я Attendance Telegram Bot и я помогу тебе быть в курсе последних новостей о посещаемости в университете.\n\n"
            +
            "Моя цель - не только фиксировать твою посещаемость, но и предоставлять полезную информацию. " +
            "Узнай свой рейтинг среди студентов, следи за своей активностью и получай уведомления о своей посещаемости. \n\n"
            +
            "Для начала, отправь мне команду /help, чтобы узнать, как я могу тебе помочь.";


    public StartCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), START_MESSAGE);
    }
}