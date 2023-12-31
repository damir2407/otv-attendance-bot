package org.example.otvattendancebotdispatcher.service;

public interface SendBotMessageService {

    void sendMessage(Long chatId, String message);
}
