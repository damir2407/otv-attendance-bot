package org.example.otvattendancebotdispatcher.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommandName {
    START("/start"),
    HELP("/help"),
    REGISTER("/register"),
    REGISTER_TEACHER("/register_teacher"),
    REGISTER_GROUP("/register_group"),
    REGISTER_SUBJECT("/register_subject"),
    ASSIGN_TEACHER("/assign_teacher"),
    MARK("/mark"),
    GET_ATTENDANCE("/get_attendance"),
    GET_GROUP_ATTENDANCE("/get_group_attendance");


    private final String commandName;
}
