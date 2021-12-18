package se.iths.java21.database.cmds;

import se.iths.java21.database.dao.CourseDao;
import se.iths.java21.database.dao.TeacherDao;
import se.iths.java21.database.entities.Course;
import se.iths.java21.database.impl.CourseDaoImpl;
import se.iths.java21.database.impl.TeacherDaoImpl;
import se.iths.java21.database.tools.Command;
import se.iths.java21.database.tools.InputHandler;

import java.sql.Date;

public class CourseCmds {
    private final Command[] commands = new Command[6];
    private final CourseDao courseDao = new CourseDaoImpl();
    private final TeacherDao teacherDao = new TeacherDaoImpl();

    public CourseCmds() {
        commands[1] = this::add;
        commands[2] = this::update;
        commands[3] = this::delete;
        commands[4] = this::filter;
        commands[5] = this::showAll;
        commands[0] = this::shutDown;
    }

    public void run() {
        int choice;

        do {
            printMenuOptions();
            choice = readChoice();
            executeChoice(choice);
        } while (choice != 0);
    }

    private void printMenuOptions() {
        System.out.println("""
                                
                             +--------+ +-----------+ +-----------+ +-----------+ +-------------+ +---------+
                COURSES      | 1. Add | | 2. Update | | 3. Delete | | 4. Filter | | 5. Show All | | 0. Exit |
                             +--------+ +-----------+ +-----------+ +-----------+ +-------------+ +---------+
                                
                Make your menu choice by writing the NUMBER and then press ENTER!
                ↓ Write Here ↓""");
    }

    private int readChoice() {
        return InputHandler.getIntegerInput();
    }

    private void executeChoice(int choice) {
        try {
            commands[choice].execute();
        } catch (ArrayIndexOutOfBoundsException ignore) {
            System.out.println("\nInvalid choice, try again");
        }
    }

    private void shutDown() {
        System.out.println("\nGoing back to Main Menu...");
    }

    public void insertDemoCourses() {
        Course course1 = new Course("Java", Date.valueOf("2021-09-01"));
        Course course2 = new Course("Database", Date.valueOf("2021-11-01"));
        Course course3 = new Course("Sewing", Date.valueOf("2021-09-01"));
        Course course4 = new Course("Knitting", Date.valueOf("2022-09-01"));
        course1.addTeacher(teacherDao.getByID(1));
        course1.addTeacher(teacherDao.getByID(2));
        course1.addTeacher(teacherDao.getByID(3));
        course2.addTeacher(teacherDao.getByID(2));
        course2.addTeacher(teacherDao.getByID(3));
        course3.addTeacher(teacherDao.getByID(1));
        course4.addTeacher(teacherDao.getByID(1));
        course4.addTeacher(teacherDao.getByID(3));
        courseDao.insert(course1);
        courseDao.insert(course2);
        courseDao.insert(course3);
        courseDao.insert(course4);
    }

    public void truncateTable() {
        courseDao.truncate();
    }
}
