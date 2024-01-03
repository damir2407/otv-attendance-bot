package org.example.otvattendancebotdispatcher.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommandName {
    START("/start"),
    HELP("/help"),
    REGISTER("/register");


    private final String commandName;
}
