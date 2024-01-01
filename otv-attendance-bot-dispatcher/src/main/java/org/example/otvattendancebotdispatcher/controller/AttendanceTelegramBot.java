package org.example.otvattendancebotdispatcher.controller;

import org.example.otvattendancebotdispatcher.command.BaseCommandContainer;
import org.example.otvattendancebotdispatcher.command.QueueCommandContainer;
import org.example.otvattendancebotdispatcher.configuration.properties.BotProperties;
import org.example.otvattendancebotdispatcher.service.SendBotMessageServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AttendanceTelegramBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final BaseCommandContainer baseCommandContainer;
    private final QueueCommandContainer queueCommandContainer;

    public AttendanceTelegramBot(BotProperties botProperties, QueueCommandContainer queueCommandContainer) {
        this.botProperties = botProperties;
        this.queueCommandContainer = queueCommandContainer;
        this.baseCommandContainer = new BaseCommandContainer(new SendBotMessageServiceImpl(this));
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            String commandIdentifier = message.split(" ")[0].toLowerCase();

            if (!queueCommandContainer.retrieveCommand(commandIdentifier, update)) {
                baseCommandContainer.retrieveCommand(commandIdentifier).execute(update);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }
}