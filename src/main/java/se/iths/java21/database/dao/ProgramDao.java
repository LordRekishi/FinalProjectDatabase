package se.iths.java21.database.dao;

import se.iths.java21.database.entities.Course;
import se.iths.java21.database.entities.Program;
import se.iths.java21.database.entities.Student;

import java.sql.Date;
import java.util.List;

public interface ProgramDao extends Dao<Program> {
    List<Program> getByStartDate(Date min, Date max);
    List<Program> getByStartDate(Date exactDate);
    Program getByCourse(Course course);
    Program getByStudent(Student student);
}
