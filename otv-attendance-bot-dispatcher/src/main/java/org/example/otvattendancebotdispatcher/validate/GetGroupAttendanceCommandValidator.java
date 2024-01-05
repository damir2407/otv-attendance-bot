package org.example.otvattendancebotdispatcher.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetGroupAttendanceCommandValidator implements Validate {

    private static final String GET_GROUP_ATTENDANCE_COMMAND_PATTERN = "/get_group_attendance\\s+\\w+";
    private static final Pattern COMMAND_PATTERN = Pattern.compile(GET_GROUP_ATTENDANCE_COMMAND_PATTERN);

    @Override
    public boolean validateCommand(String text) {
        Matcher matcher = COMMAND_PATTERN.matcher(text);
        return matcher.matches();
    }
}