package org.example.otvattendancebotnode.jms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceQueueModel {

    private String text;
    private Long chatId;
    private String telegramName;
    private Long telegramUserId;

    public AttendanceQueueModel(String text, Long chatId) {
        this.text = text;
        this.chatId = chatId;
    }
}
