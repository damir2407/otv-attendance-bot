package org.example.otvattendancebotdispatcher.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface SendQueueMessageService {

    void sendToAttendanceSendQueue(Update update);
}
