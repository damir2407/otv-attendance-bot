package org.example.otvattendancebotdispatcher.service.jms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceQueueModel {

    private String text;
    private Long chatId;
}
