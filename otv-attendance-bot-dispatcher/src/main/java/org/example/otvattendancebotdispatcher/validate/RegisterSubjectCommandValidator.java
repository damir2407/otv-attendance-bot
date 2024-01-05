package org.example.otvattendancebotdispatcher.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterSubjectCommandValidator implements Validate {

    private static final String REGISTER_SUBJECT_COMMAND_PATTERN = "/register_subject\\s+.+";
    private static final Pattern COMMAND_PATTERN = Pattern.compile(REGISTER_SUBJECT_COMMAND_PATTERN);

    @Override
    public boolean validateCommand(String text) {
        Matcher matcher = COMMAND_PATTERN.matcher(text);

        return matcher.matches();
    }
}

