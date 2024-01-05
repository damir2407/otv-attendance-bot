package org.example.otvattendancebotnode.util;

import java.util.Comparator;
import org.example.otvattendancebotnode.entity.Student;

public class StudentComparator implements Comparator<Student> {

    @Override
    public int compare(Student student1, Student student2) {
        return Integer.compare(student1.getAttendances().size(), student2.getAttendances().size());
    }
}