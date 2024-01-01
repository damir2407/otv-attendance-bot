package org.example.otvattendancebotdispatcher.configuration.jms.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceSendQueueModel {

    private String text;
    private Long chatId;
}
