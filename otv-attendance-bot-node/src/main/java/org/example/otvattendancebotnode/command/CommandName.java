package org.example.otvattendancebotnode.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommandName {
    START("/start"),
    HELP("/help");

    private final String commandName;
}
