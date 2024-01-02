package org.example.otvattendancebotnode.command;

import org.example.otvattendancebotnode.jms.model.AttendanceQueueModel;

public interface Command {

    void execute(AttendanceQueueModel model);
}
