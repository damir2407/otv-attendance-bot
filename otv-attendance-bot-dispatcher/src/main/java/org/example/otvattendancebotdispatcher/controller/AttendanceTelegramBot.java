package org.example.otvattendancebotdispatcher.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.otvattendancebotdispatcher.command.CommandContainer;
import org.example.otvattendancebotdispatcher.configuration.properties.BotProperties;
import org.example.otvattendancebotdispatcher.service.SendBotMessageServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class AttendanceTelegramBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final CommandContainer commandContainer;

    public AttendanceTelegramBot(BotProperties botProperties) {
        this.botProperties = botProperties;
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this));
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();

            String commandIdentifier = message.split(" ")[0].toLowerCase();

            commandContainer.retrieveCommand(commandIdentifier).execute(update);
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