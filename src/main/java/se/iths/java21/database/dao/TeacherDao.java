package se.iths.java21.database.dao;

import se.iths.java21.database.entities.Course;
import se.iths.java21.database.entities.Teacher;

import java.sql.Date;
import java.util.List;

public interface TeacherDao extends Dao<Teacher> {
    List<Teacher> getByStartOfEmployment(Date min, Date max);
    List<Teacher> getByCourse(Course course);
}
