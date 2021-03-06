package se.iths.java21.database.cmds;

import se.iths.java21.database.dao.CourseDao;
import se.iths.java21.database.dao.ProgramDao;
import se.iths.java21.database.dao.StudentDao;
import se.iths.java21.database.entities.Course;
import se.iths.java21.database.entities.Program;
import se.iths.java21.database.entities.Student;
import se.iths.java21.database.impl.CourseDaoImpl;
import se.iths.java21.database.impl.ProgramDaoImpl;
import se.iths.java21.database.impl.StudentDaoImpl;
import se.iths.java21.database.tools.Command;
import se.iths.java21.database.tools.InputHandler;

import java.sql.Date;
import java.util.List;

public class ProgramCmds {
    private final Command[] commands = new Command[6];
    private final ProgramDao programDao = new ProgramDaoImpl();
    private final CourseDao courseDao = new CourseDaoImpl();
    private final StudentDao studentDao = new StudentDaoImpl();

    public ProgramCmds() {
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
                PROGRAMS     | 1. Add | | 2. Update | | 3. Delete | | 4. Filter | | 5. Show All | | 0. Exit |
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

    public void insertDemoPrograms() {
        Course course1 = courseDao.getByID(1);
        Course course2 = courseDao.getByID(2);
        Course course3 = courseDao.getByID(3);
        Course course4 = courseDao.getByID(4);

        Student student1 = studentDao.getByID(1);
        Student student2 = studentDao.getByID(2);
        Student student3 = studentDao.getByID(3);
        Student student4 = studentDao.getByID(4);
        Student student5 = studentDao.getByID(5);

        Program program1 = new Program("Java Programming", Date.valueOf("2021-09-01"));
        programDao.insert(program1);

        Program program2 = new Program("Clothes design", Date.valueOf("2010-09-01"));
        programDao.insert(program2);

        course1.setProgram(program1);
        courseDao.update(course1);
        course2.setProgram(program1);
        courseDao.update(course2);
        course3.setProgram(program2);
        courseDao.update(course3);
        course4.setProgram(program2);
        courseDao.update(course4);

        student1.setProgram(program1);
        studentDao.update(student1);
        student2.setProgram(program1);
        studentDao.update(student2);
        student3.setProgram(program1);
        studentDao.update(student3);
        student4.setProgram(program2);
        studentDao.update(student4);
        student5.setProgram(program2);
        studentDao.update(student5);
    }

    public void truncateTable() {
        programDao.truncate();
    }

    private void add() {
        System.out.println("\n1. Adding new Program!");

        System.out.println("\nProgram name:");
        String name = InputHandler.getStringInput();
        System.out.println("\nStart Date (Date format " + new Date(System.currentTimeMillis()) + "):");
        Date start_date = Date.valueOf(InputHandler.getStringInput());

        Program program = new Program(name, start_date);
        programDao.insert(program);

        System.out.println("\nAdd Courses to Program? (Y/N)");
        if (InputHandler.getStringInput().equalsIgnoreCase("y")) {
            do {
                System.out.println("\nPlease enter ID of Course to add:");
                Course course = courseDao.getByID(InputHandler.getIntegerInput());

                course.setProgram(program);
                courseDao.update(course);

                System.out.println("\nAdd another Course? (Y/N)");
            } while (!InputHandler.getStringInput().equalsIgnoreCase("n"));
        }

        System.out.println("\nAdd Students to Program? (Y/N)");
        if (InputHandler.getStringInput().equalsIgnoreCase("y")) {
            do {
                System.out.println("\nPlease enter ID of Student to add:");
                Student student = studentDao.getByID(InputHandler.getIntegerInput());

                student.setProgram(program);
                studentDao.update(student);

                System.out.println("\nAdd another Student? (Y/N)");
            } while (!InputHandler.getStringInput().equalsIgnoreCase("n"));
        }

        System.out.println("\nNew Program added successfully!");
    }

    private void update() {
        System.out.println("\n2. Updating Program information");

        System.out.println("\nPlease enter Program ID:");
        Program program = programDao.getByID(InputHandler.getIntegerInput());

        System.out.println("\nSELECTED: " + program);

        updateMenuChoice(program);

        programDao.update(program);
    }

    private void updateMenuChoice(Program program) {
        int choice;

        do {
            System.out.println("""
                                        
                    What do you want to Update?
                                        
                    1. Name
                    2. Start Date
                    3. Courses
                    4. Students
                    0. Back to menu
                                        
                    Make your menu choice by writing the NUMBER and then press ENTER!
                    ↓ Write Here ↓""");
            choice = InputHandler.getIntegerInput();

            switch (choice) {
                case 1 -> updateName(program);
                case 2 -> updateStartDate(program);
                case 3 -> updateCourses(program);
                case 4 -> updateStudents(program);
                case 0 -> System.out.println("\nReturning to menu...");
                default -> System.out.println("\nInvalid choice, try again");
            }

        } while (choice != 0);
    }

    private void updateName(Program program) {
        System.out.println("\n1. Updating Program Name");
        System.out.println("Program current Name: " + program.getName());

        System.out.println("\nPlease enter new Name:");
        String name = InputHandler.getStringInput();
        program.setName(name);

        updatedPrint(program);
    }

    private void updateStartDate(Program program) {
        System.out.println("\n2. Updating Start Date");

        System.out.println("\nProgram current Start Date: " + program.getStart_date().toString());

        System.out.println("\nPlease enter new Start Date (Date format " + new Date(System.currentTimeMillis()) + "):");
        Date startDate = Date.valueOf(InputHandler.getStringInput());
        program.setStart_date(startDate);

        updatedPrint(program);
    }

    private void updateCourses(Program program) {
        System.out.println("\n3. Updating Courses");

        System.out.println("\nCurrent Courses in Program");
        List<Course> courses = courseDao.getByProgram(program);
        courses.forEach(System.out::println);

        System.out.println("\nPlease enter ID of Course:");
        Course course = courseDao.getByID(InputHandler.getIntegerInput());

        System.out.println("\nADD or DELETE selected Course? (A/D)");
        String input;
        do {
            input = InputHandler.getStringInput();
            if (input.equalsIgnoreCase("d")) {
                if (courses.contains(course)) {
                    course.setProgram(null);
                    courseDao.update(course);
                    break;
                } else {
                    System.out.println("\nCan't find selected Course...");
                }
            } else if (input.equalsIgnoreCase("a")) {
                if (!courses.contains(course)) {
                    course.setProgram(program);
                    courseDao.update(course);
                    break;
                } else {
                    System.out.println("\nCan't add same Course twice...");
                }
            } else {
                System.out.println("Invalid choice! Try again!");
            }
        } while (true);

        updatedPrint(program);
    }

    private void updateStudents(Program program) {
        System.out.println("\n4. Updating Students");

        System.out.println("\nProgram current Students:");
        List<Student> students = studentDao.getByProgram(program);
        students.forEach(System.out::println);

        System.out.println("\nPlease enter ID of Student:");
        Student student = studentDao.getByID(InputHandler.getIntegerInput());

        System.out.println("\nADD or DELETE selected Student? (A/D)");
        String input;
        do {
            input = InputHandler.getStringInput();
            if (input.equalsIgnoreCase("d")) {
                if (students.contains(student)) {
                    student.setProgram(null);
                    studentDao.update(student);
                    break;
                } else {
                    System.out.println("\nCan't find selected Student...");
                }
            } else if (input.equalsIgnoreCase("a")) {
                if (!students.contains(student)) {
                    student.setProgram(program);
                    studentDao.update(student);
                    break;
                } else {
                    System.out.println("\nCan't add same Student twice...");
                }
            } else {
                System.out.println("Invalid choice! Try again!");
            }
        } while (true);

        updatedPrint(program);
    }

    private void updatedPrint(Program program) {
        System.out.println("\nProgram was updated successfully!");
        System.out.println("Updated info: " + program);

        System.out.println("\nReturning to menu...");
    }

    private void delete() {
        deletePrint();
        int id = InputHandler.getIntegerInput();

        if (id == 0) {
            System.out.println("\nCancelling and returning to menu...");
            return;
        }

        Program programToDelete = programDao.getByID(id);

        studentDao.getAll().stream()
                .filter(student -> student.getProgram().equals(programToDelete))
                .forEach(student -> student.setProgram(null));

        studentDao.getAll().forEach(studentDao::update);

        courseDao.getAll().stream()
                .filter(course -> course.getProgram().equals(programToDelete))
                .forEach(course -> course.setProgram(null));

        courseDao.getAll().forEach(courseDao::update);

        programDao.delete(programToDelete);

        deleteExitPrint();
    }

    private void deletePrint() {
        System.out.println("\n3. Deleting Program");

        System.out.println("\nList of All Programs:");
        allProgramsPrint();

        System.out.println("""
                                
                Please enter ID of Program to Delete:
                                
                !! WARNING THIS IS PERMANENT !!
                !! Write 0 to cancel !!
                                
                ↓ Write Here ↓""");
    }

    private void deleteExitPrint() {
        System.out.println("\nProgram Deleted successfully!");

        System.out.println("\nNew List of All Programs:");
        allProgramsPrint();

        System.out.println("\nReturning to menu...");
    }

    private void filter() {
        System.out.println("\n4. Show Filtered list of Programs");

        int choice;

        do {
            System.out.println("""
                                        
                    Filter Programs by what?
                                        
                    1. ID
                    2. Name
                    3. Start Date
                    4. Courses
                    5. Students
                    0. Back to menu
                                        
                    Make your menu choice by writing the NUMBER and then press ENTER!
                    ↓ Write Here ↓""");
            choice = InputHandler.getIntegerInput();

            switch (choice) {
                case 1 -> getByID();
                case 2 -> getByName();
                case 3 -> getByStartDate();
                case 4 -> getByCourse();
                case 5 -> getByStudent();
                case 0 -> System.out.println("\nReturning to menu...");
                default -> System.out.println("\nInvalid choice, try again");
            }

        } while (choice != 0);

    }

    private void getByID() {
        System.out.println("\nPlease enter ID:");
        int id = InputHandler.getIntegerInput();

        System.out.println("\nResult:");
        System.out.println(programDao.getByID(id));
    }

    private void getByName() {
        System.out.println("\nPlease enter whole or part of a Name:");
        String name = InputHandler.getStringInput();

        System.out.println("\nResult:");
        programDao.getByName(name).forEach(System.out::println);
    }

    private void getByStartDate() {
        System.out.println("\nPlease enter RANGE or EXACT Start Date (Date format " + new Date(System.currentTimeMillis()) + "):");

        System.out.println("\nMinimum Start Date:");
        Date min = Date.valueOf(InputHandler.getStringInput());
        System.out.println("Maximum Start Date:");
        Date max = Date.valueOf(InputHandler.getStringInput());

        System.out.println("\nResult:");
        if (min == max) {
            programDao.getByStartDate(min).forEach(System.out::println);
        } else {
            programDao.getByStartDate(min, max).forEach(System.out::println);
        }
    }

    private void getByCourse() {
        System.out.println("\nPlease enter ID of Course:");
        int id = InputHandler.getIntegerInput();
        Course course = courseDao.getByID(id);

        System.out.println("\nResult:");
        System.out.println(programDao.getByCourse(course));
    }

    private void getByStudent() {
        System.out.println("\nPlease enter ID of Student:");
        int id = InputHandler.getIntegerInput();
        Student student = studentDao.getByID(id);

        System.out.println("\nResult:");
        System.out.println(programDao.getByStudent(student));
    }

    private void showAll() {
        System.out.println("\n5. Show list of All Programs");
        allProgramsPrint();
    }

    private void allProgramsPrint() {
        programDao.getAll().forEach(System.out::println);
    }
}
