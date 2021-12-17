package se.iths.java21.database.dao;

import se.iths.java21.database.entities.Program;
import se.iths.java21.database.entities.Student;

import java.sql.Date;
import java.util.List;

public interface StudentDao extends Dao<Student> {
    List<Student> getByDateOfBirth(Date min, Date max);
    List<Student> getByIsActive(boolean input);
    List<Student> getByProgram(Program program);
}
