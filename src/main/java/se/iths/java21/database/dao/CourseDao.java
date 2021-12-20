package se.iths.java21.database.dao;

import se.iths.java21.database.entities.Course;
import se.iths.java21.database.entities.Program;
import se.iths.java21.database.entities.Teacher;

import java.sql.Date;
import java.util.List;

public interface CourseDao extends Dao<Course> {
    List<Course> getByStartDate(Date min, Date max);
    List<Course> getByStartDate(Date exactDate);
    List<Course> getByProgram(Program program);
    List<Course> getByTeacher(Teacher teacher);
}
