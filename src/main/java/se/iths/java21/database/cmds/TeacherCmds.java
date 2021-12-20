package se.iths.java21.database.cmds;

import se.iths.java21.database.dao.CourseDao;
import se.iths.java21.database.dao.TeacherDao;
import se.iths.java21.database.entities.Course;
import se.iths.java21.database.entities.Teacher;
import se.iths.java21.database.impl.CourseDaoImpl;
import se.iths.java21.database.impl.TeacherDaoImpl;
import se.iths.java21.database.tools.Command;
import se.iths.java21.database.tools.InputHandler;

import java.sql.Date;
import java.util.List;

public class TeacherCmds {
    private final Command[] commands = new Command[6];
    private final TeacherDao teacherDao = new TeacherDaoImpl();
    private final CourseDao courseDao = new CourseDaoImpl();

    public TeacherCmds() {
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
                TEACHERS     | 1. Add | | 2. Update | | 3. Delete | | 4. Filter | | 5. Show All | | 0. Exit |
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

    public void insertDemoTeachers() {
        Teacher teacher1 = new Teacher("Greger", "Smörgås", Date.valueOf("2020-09-01"));
        Teacher teacher2 = new Teacher("Jenny", "Trevlig", Date.valueOf("1980-09-01"));
        Teacher teacher3 = new Teacher("Tina", "Bondtös", Date.valueOf("2021-09-01"));
        teacherDao.insert(teacher1);
        teacherDao.insert(teacher2);
        teacherDao.insert(teacher3);
    }

    public void truncateTable() {
        teacherDao.truncate();
    }

    private void add() {
        System.out.println("\n1. Adding new Teacher!");

        System.out.println("\nFirst name:");
        String firstName = InputHandler.getStringInput();
        System.out.println("\nLast name:");
        String lastName = InputHandler.getStringInput();
        System.out.println("\nStart of Employment (Date format " + new Date(System.currentTimeMillis()) + "):");
        Date start_date = Date.valueOf(InputHandler.getStringInput());

        Teacher teacher = new Teacher(firstName, lastName, start_date);
        teacherDao.insert(teacher);

        System.out.println("\nAdd Teacher to a Course? (Y/N)");
        Course course;
        if (InputHandler.getStringInput().equalsIgnoreCase("y")) {
            System.out.println("\nPlease enter ID of Course to add teacher to:");
            course = courseDao.getByID(InputHandler.getIntegerInput());

            course.addTeacher(teacher);
            courseDao.update(course);
        }

        System.out.println("\nNew Teacher added successfully!");
    }

    private void update() {
        System.out.println("\n2. Updating Teacher information");

        System.out.println("\nPlease enter Teacher ID:");
        Teacher teacher = teacherDao.getByID(InputHandler.getIntegerInput());

        System.out.println("\nSELECTED: " + teacher);

        updateMenuChoice(teacher);
        teacherDao.update(teacher);
    }

    private void updateMenuChoice(Teacher teacher) {
        int choice;

        do {
            System.out.println("""
                                        
                    What do you want to Update?
                                        
                    1. First name
                    2. Last name
                    3. Start of Employment
                    4. Courses taught
                    0. Back to menu
                                        
                    Make your menu choice by writing the NUMBER and then press ENTER!
                    ↓ Write Here ↓""");
            choice = InputHandler.getIntegerInput();

            switch (choice) {
                case 1 -> updateFirstName(teacher);
                case 2 -> updateLastName(teacher);
                case 3 -> updateStartDate(teacher);
                case 4 -> updateCourses(teacher);
                case 0 -> System.out.println("\nReturning to menu...");
                default -> System.out.println("\nInvalid choice, try again");
            }

        } while (choice != 0);
    }

    private void updateFirstName(Teacher teacher) {
        System.out.println("\n1. Updating First Name");
        System.out.println("Teacher current First Name: " + teacher.getFirst_name());

        System.out.println("\nPlease enter new First Name:");
        String name = InputHandler.getStringInput();
        teacher.setFirst_name(name);

        updatedPrint(teacher);
    }

    private void updateLastName(Teacher teacher) {
        System.out.println("\n2. Updating Last Name");
        System.out.println("Teacher current Last Name: " + teacher.getLast_name());

        System.out.println("\nPlease enter new Last Name:");
        String name = InputHandler.getStringInput();
        teacher.setLast_name(name);

        updatedPrint(teacher);
    }

    private void updateStartDate(Teacher teacher) {
        System.out.println("\n3. Updating Start of Employment");
        System.out.println("Student current Start of Employment: " + teacher.getStart_of_employment().toString());

        System.out.println("\nPlease enter new Start of Employment (Date format " + new Date(System.currentTimeMillis()) + "):");
        Date startDate = Date.valueOf(InputHandler.getStringInput());
        teacher.setStart_of_employment(startDate);

        updatedPrint(teacher);
    }

    private void updateCourses(Teacher teacher) {
        System.out.println("\n4. Updating Courses");

        System.out.println("\nPlease enter ID of Course:");
        Course course = courseDao.getByID(InputHandler.getIntegerInput());

        System.out.println("\nCourse current Teachers:");
        course.getTeachers().forEach(System.out::println);

        System.out.println("\nADD or DELETE selected Teacher from Course? (A/D)");
        String input;

        do {
            input = InputHandler.getStringInput();
            if (input.equalsIgnoreCase("d")) {
                course.deleteTeacher(teacher);
                courseDao.update(course);
                break;
            } else if (input.equalsIgnoreCase("a")) {
                course.addTeacher(teacher);
                courseDao.update(course);
                break;
            } else {
                System.out.println("Invalid choice! Try again!");
            }
        } while (true);

        updatedPrint(teacher);
    }

    private void updatedPrint(Teacher teacher) {
        System.out.println("\nTeacher was updated successfully!");
        System.out.println("Updated info: " + teacher);

        System.out.println("\nReturning to menu...");
    }

    private void delete() {
        deletePrint();
        int id = InputHandler.getIntegerInput();

        if (id == 0) {
            System.out.println("\nCancelling and returning to menu...");
            return;
        }


        Teacher teacherToDelete = teacherDao.getByID(id);

        courseDao.getAll().stream()
                .filter(course -> course.getTeachers().contains(teacherToDelete))
                        .forEach(course -> course.deleteTeacher(teacherToDelete));

        courseDao.getAll().forEach(courseDao::update);

        teacherDao.delete(teacherToDelete);

        deleteExitPrint();
    }

    private void deletePrint() {
        System.out.println("\n3. Deleting Teacher");

        System.out.println("\nList of All Teachers:");
        allTeachersPrint();

        System.out.println("""
                                
                Please enter ID of Teacher to Delete:
                                
                !! WARNING THIS IS PERMANENT !!
                !! Write 0 to cancel !!
                                
                ↓ Write Here ↓""");
    }

    private void deleteExitPrint() {
        System.out.println("\nTeacher Deleted successfully!");

        System.out.println("\nNew List of All Teachers:");
        allTeachersPrint();

        System.out.println("\nReturning to menu...");
    }

    private void filter() {
        System.out.println("\n4. Show Filtered list of Teachers");

        int choice;

        do {
            System.out.println("""
                                        
                    Filter Teachers by what?
                                        
                    1. ID
                    2. Name
                    3. Start of Employment
                    4. Course
                    0. Back to menu
                                        
                    Make your menu choice by writing the NUMBER and then press ENTER!
                    ↓ Write Here ↓""");
            choice = InputHandler.getIntegerInput();

            switch (choice) {
                case 1 -> getByID();
                case 2 -> getByName();
                case 3 -> getByStartOfEmployment();
                case 4 -> getByCourses();
                case 0 -> System.out.println("\nReturning to menu...");
                default -> System.out.println("\nInvalid choice, try again");
            }

        } while (choice != 0);

    }

    private void getByID() {
        System.out.println("\nPlease enter ID:");
        int id = InputHandler.getIntegerInput();

        System.out.println("\nResult:");
        System.out.println(teacherDao.getByID(id));
    }

    private void getByName() {
        System.out.println("\nPlease enter whole or part of a Name:");
        String name = InputHandler.getStringInput();

        System.out.println("\nResult:");
        teacherDao.getByName(name).forEach(System.out::println);
    }

    private void getByStartOfEmployment() {
        System.out.println("\nPlease enter range of Start of Employment (Date format " + new Date(System.currentTimeMillis()) + "):");

        System.out.println("\nMinimum Start Date:");
        Date min = Date.valueOf(InputHandler.getStringInput());
        System.out.println("Maximum Start Date:");
        Date max = Date.valueOf(InputHandler.getStringInput());

        System.out.println("\nResult:");
        teacherDao.getByStartOfEmployment(min, max).forEach(System.out::println);
    }


    private void getByCourses() {
        System.out.println("\nPlease enter ID of Course:");
        int id = InputHandler.getIntegerInput();
        Course course = courseDao.getByID(id);

        System.out.println("\nResult:");
        teacherDao.getByCourse(course).forEach(System.out::println);
    }

    private void showAll() {
        System.out.println("\n5. Show list of All Teachers");
        allTeachersPrint();
    }

    private void allTeachersPrint() {
        teacherDao.getAll().forEach(System.out::println);
    }
}
