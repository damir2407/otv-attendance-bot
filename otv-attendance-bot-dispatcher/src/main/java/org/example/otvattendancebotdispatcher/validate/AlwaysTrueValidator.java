package org.example.otvattendancebotdispatcher.validate;

public class AlwaysTrueValidator implements Validate {

    @Override
    public boolean validateCommand(String text) {
        return true;
    }
}
