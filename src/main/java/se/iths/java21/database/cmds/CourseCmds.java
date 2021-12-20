package se.iths.java21.database.cmds;

import se.iths.java21.database.dao.CourseDao;
import se.iths.java21.database.dao.ProgramDao;
import se.iths.java21.database.dao.TeacherDao;
import se.iths.java21.database.entities.Course;
import se.iths.java21.database.entities.Program;
import se.iths.java21.database.entities.Teacher;
import se.iths.java21.database.impl.CourseDaoImpl;
import se.iths.java21.database.impl.ProgramDaoImpl;
import se.iths.java21.database.impl.TeacherDaoImpl;
import se.iths.java21.database.tools.Command;
import se.iths.java21.database.tools.InputHandler;

import java.sql.Date;

public class CourseCmds {
    private final Command[] commands = new Command[6];
    private final CourseDao courseDao = new CourseDaoImpl();
    private final TeacherDao teacherDao = new TeacherDaoImpl();
    private final ProgramDao programDao = new ProgramDaoImpl();

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
        Teacher teacher1 = teacherDao.getByID(1);
        Teacher teacher2 = teacherDao.getByID(2);
        Teacher teacher3 = teacherDao.getByID(3);

        Course course1 = new Course("Java", Date.valueOf("2021-09-01"));
        course1.addTeacher(teacher1);
        course1.addTeacher(teacher2);
        course1.addTeacher(teacher3);
        courseDao.insert(course1);

        Course course2 = new Course("Database", Date.valueOf("2021-11-01"));
        course2.addTeacher(teacher2);
        course2.addTeacher(teacher3);
        courseDao.insert(course2);

        Course course3 = new Course("Sewing", Date.valueOf("2021-09-01"));
        course3.addTeacher(teacher1);
        courseDao.insert(course3);

        Course course4 = new Course("Knitting", Date.valueOf("2022-09-01"));
        course4.addTeacher(teacher1);
        course4.addTeacher(teacher3);
        courseDao.insert(course4);

        teacherDao.update(teacher1);
        teacherDao.update(teacher2);
        teacherDao.update(teacher3);
    }

    public void truncateTable() {
        courseDao.truncate();
    }

    private void add() {
        System.out.println("\n1. Adding new Course!");

        System.out.println("\nCourse name:");
        String name = InputHandler.getStringInput();

        System.out.println("\nStart Date (Date format " + new Date(System.currentTimeMillis()) + "):");
        Date start_date = Date.valueOf(InputHandler.getStringInput());

        Course course = new Course(name, start_date);
        courseDao.insert(course);

        System.out.println("\nAdd Course to a Program? (Y/N)");
        if (InputHandler.getStringInput().equalsIgnoreCase("y")) {
            System.out.println("\nPlease enter ID of Program to add Course to:");
            Program program = programDao.getByID(InputHandler.getIntegerInput());

            course.setProgram(program);
        }

        System.out.println("\nAdd a Teacher to this Course? (Y/N)");
        if (InputHandler.getStringInput().equalsIgnoreCase("y")) {
            System.out.println("\nPlease enter ID of Teacher to add:");
            Teacher teacher = teacherDao.getByID(InputHandler.getIntegerInput());

            course.addTeacher(teacher);
        }
        courseDao.update(course);

        System.out.println("\nNew Course added successfully!");
    }

    private void update() {
        System.out.println("\n2. Updating Course information");

        System.out.println("\nPlease enter Course ID:");
        Course course = courseDao.getByID(InputHandler.getIntegerInput());

        System.out.println("\nSELECTED: " + course);

        updateMenuChoice(course);

        courseDao.update(course);
    }

    private void updateMenuChoice(Course course) {
        int choice;

        do {
            System.out.println("""
                                        
                    What do you want to Update?
                                        
                    1. Name
                    2. Start Date
                    3. Program
                    4. Teachers
                    0. Back to menu
                                        
                    Make your menu choice by writing the NUMBER and then press ENTER!
                    ↓ Write Here ↓""");
            choice = InputHandler.getIntegerInput();

            switch (choice) {
                case 1 -> updateName(course);
                case 2 -> updateStartDate(course);
                case 3 -> updateProgram(course);
                case 4 -> updateTeachers(course);
                case 0 -> System.out.println("\nReturning to menu...");
                default -> System.out.println("\nInvalid choice, try again");
            }

        } while (choice != 0);
    }

    private void updateName(Course course) {
        System.out.println("\n1. Updating Course Name");
        System.out.println("Course current Name: " + course.getName());

        System.out.println("\nPlease enter new Name:");
        String name = InputHandler.getStringInput();
        course.setName(name);

        updatedPrint(course);
    }

    private void updateStartDate(Course course) {
        System.out.println("\n2. Updating Start Date");
        System.out.println("Course current Start Date: " + course.getStart_date().toString());

        System.out.println("\nPlease enter new Start Date (Date format " + new Date(System.currentTimeMillis()) + "):");
        Date startDate = Date.valueOf(InputHandler.getStringInput());
        course.setStart_date(startDate);

        updatedPrint(course);
    }

    private void updateProgram(Course course) {
        System.out.println("\n3. Updating Program");
        System.out.println("Course current Program: " + course.getProgram().getName());

        System.out.println("\nPlease enter ID of new Program:");
        Program program = programDao.getByID(InputHandler.getIntegerInput());

        course.setProgram(program);

        updatedPrint(course);
    }

    private void updateTeachers(Course course) {
        System.out.println("\n4. Updating Teachers");
        System.out.println("\nCourse current Teachers:");
        course.getTeachers().forEach(System.out::println);

        System.out.println("\nPlease enter ID of Teacher:");
        Teacher teacher = teacherDao.getByID(InputHandler.getIntegerInput());

        System.out.println("\nDELETE or ADD selected Teacher? (D/A)");
        String input;

        do {
            input = InputHandler.getStringInput();
            if (input.equalsIgnoreCase("d")) {
                course.deleteTeacher(teacher);
                break;
            } else if (input.equalsIgnoreCase("a")) {
                course.addTeacher(teacher);
                break;
            } else {
                System.out.println("Invalid choice! Try again!");
            }
        } while (true);

        updatedPrint(course);
    }

    private void updatedPrint(Course course) {
        System.out.println("\nCourse was updated successfully!");
        System.out.println("Updated info: " + course);

        System.out.println("\nReturning to menu...");
    }

    private void delete() {
        deletePrint();
        int id = InputHandler.getIntegerInput();

        if (id == 0) {
            System.out.println("\nCancelling and returning to menu...");
            return;
        }

        Course courseToDelete = courseDao.getByID(id);
        courseDao.delete(courseToDelete);

        deleteExitPrint();
    }

    private void deletePrint() {
        System.out.println("\n3. Deleting Course");

        System.out.println("\nList of All Courses:");
        allCoursesPrint();

        System.out.println("""
                                
                Please enter ID of Course to Delete:
                                
                !! WARNING THIS IS PERMANENT !!
                !! Write 0 to cancel !!
                                
                ↓ Write Here ↓""");
    }

    private void deleteExitPrint() {
        System.out.println("\nCourse Deleted successfully!");

        System.out.println("\nNew List of All Courses:");
        allCoursesPrint();

        System.out.println("\nReturning to menu...");
    }

    private void filter() {
        System.out.println("\n4. Show Filtered list of Courses");

        int choice;

        do {
            System.out.println("""
                                        
                    Filter Courses by what?
                                        
                    1. ID
                    2. Name
                    3. Start Date
                    4. Program
                    5. Teachers
                    0. Back to menu
                                        
                    Make your menu choice by writing the NUMBER and then press ENTER!
                    ↓ Write Here ↓""");
            choice = InputHandler.getIntegerInput();

            switch (choice) {
                case 1 -> getByID();
                case 2 -> getByName();
                case 3 -> getByStartDate();
                case 4 -> getByProgram();
                case 5 -> getByTeachers();
                case 0 -> System.out.println("\nReturning to menu...");
                default -> System.out.println("\nInvalid choice, try again");
            }

        } while (choice != 0);

    }

    private void getByID() {
        System.out.println("\nPlease enter ID:");
        int id = InputHandler.getIntegerInput();

        System.out.println("\nResult:");
        System.out.println(courseDao.getByID(id));
    }

    private void getByName() {
        System.out.println("\nPlease enter whole or part of a Name:");
        String name = InputHandler.getStringInput();

        System.out.println("\nResult:");
        courseDao.getByName(name).forEach(System.out::println);
    }

    private void getByStartDate() {
        System.out.println("\nPlease enter RANGE or EXACT Start Date (Date format " + new Date(System.currentTimeMillis()) + "):");

        System.out.println("\nMinimum Start Date:");
        Date min = Date.valueOf(InputHandler.getStringInput());
        System.out.println("Maximum Start Date:");
        Date max = Date.valueOf(InputHandler.getStringInput());

        System.out.println("\nResult:");
        if (min == max) {
            courseDao.getByStartDate(min).forEach(System.out::println);
        } else {
            courseDao.getByStartDate(min, max).forEach(System.out::println);
        }
    }

    private void getByProgram() {
        System.out.println("\nPlease enter ID of Program:");
        int id = InputHandler.getIntegerInput();
        Program program = programDao.getByID(id);

        System.out.println("\nResult:");
        courseDao.getByProgram(program).forEach(System.out::println);
    }

    private void getByTeachers() {
        System.out.println("\nPlease enter ID of Teacher");
        int id = InputHandler.getIntegerInput();
        Teacher teacher = teacherDao.getByID(id);

        System.out.println("\nResult:");
        courseDao.getByTeacher(teacher).forEach(System.out::println);
    }

    private void showAll() {
        System.out.println("\n5. Show list of All Courses");
        allCoursesPrint();
    }

    private void allCoursesPrint() {
        courseDao.getAll().forEach(System.out::println);
    }
}
