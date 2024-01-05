package org.example.otvattendancebotdispatcher.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterCommandValidator implements Validate {

    private static final String REGISTER_COMMAND_PATTERN = "/register\\s+\\w+\\s+[А-Яа-я]+\\s+[А-Яа-я]+\\s+\\d+";
    private static final Pattern COMMAND_PATTERN = Pattern.compile(REGISTER_COMMAND_PATTERN);


    @Override
    public boolean validateCommand(String text) {
        Matcher matcher = COMMAND_PATTERN.matcher(text);
        return matcher.matches();
    }
}
