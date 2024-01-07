package org.example.otvattendancebotdispatcher.bot;

import lombok.extern.slf4j.Slf4j;
import org.example.otvattendancebotdispatcher.command.CommandContainer;
import org.example.otvattendancebotdispatcher.configuration.properties.BotProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class AttendanceTelegramBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final CommandContainer commandContainer;
    private static final String UNKNOWN_MESSAGE = "Не понимаю вас \uD83D\uDE1F. Напишите /help чтобы узнать список доступных команд и их шаблоны.";


    public AttendanceTelegramBot(BotProperties botProperties, CommandContainer commandContainer) {
        super(botProperties.getToken());
        this.botProperties = botProperties;
        this.commandContainer = commandContainer;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();

            var result = commandContainer.retrieveCommand(message, update);
            if (!result) {
                sendUnknownCommandMessage(update.getMessage().getChatId());
            }
        }
    }

    private void sendUnknownCommandMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(UNKNOWN_MESSAGE);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

}