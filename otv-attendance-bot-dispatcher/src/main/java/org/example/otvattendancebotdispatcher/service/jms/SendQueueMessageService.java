package org.example.otvattendancebotdispatcher.service.jms;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface SendQueueMessageService {

    void sendToAttendanceSendQueue(Update update);
}
