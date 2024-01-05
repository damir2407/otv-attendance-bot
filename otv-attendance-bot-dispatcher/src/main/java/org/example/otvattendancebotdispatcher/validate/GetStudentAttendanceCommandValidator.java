package org.example.otvattendancebotdispatcher.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetStudentAttendanceCommandValidator implements Validate {

    private static final String GET_ATTENDANCE_COMMAND_PATTERN = "/get_attendance\\s+\\d+";
    private static final Pattern COMMAND_PATTERN = Pattern.compile(GET_ATTENDANCE_COMMAND_PATTERN);

    @Override
    public boolean validateCommand(String text) {
        Matcher matcher = COMMAND_PATTERN.matcher(text);
        return matcher.matches();
    }
}