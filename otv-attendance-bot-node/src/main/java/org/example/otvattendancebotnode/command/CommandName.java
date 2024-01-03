package org.example.otvattendancebotnode.command;

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
    REGISTER_SUBJECT("/register_subject");

    private final String commandName;
}
