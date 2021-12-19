package se.iths.java21.database.cmds;

import se.iths.java21.database.dao.CourseDao;
import se.iths.java21.database.dao.ProgramDao;
import se.iths.java21.database.dao.StudentDao;
import se.iths.java21.database.entities.Course;
import se.iths.java21.database.entities.Program;
import se.iths.java21.database.entities.Student;
import se.iths.java21.database.entities.Teacher;
import se.iths.java21.database.impl.CourseDaoImpl;
import se.iths.java21.database.impl.ProgramDaoImpl;
import se.iths.java21.database.impl.StudentDaoImpl;
import se.iths.java21.database.tools.Command;
import se.iths.java21.database.tools.InputHandler;

import java.sql.Date;

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
        Program program1 = new Program("Java Programming", Date.valueOf("2021-09-01"));
        Program program2 = new Program("Clothes design", Date.valueOf("2010-09-01"));

        Course course1 = courseDao.getByID(1);
        Course course2 = courseDao.getByID(2);
        Course course3 = courseDao.getByID(3);
        Course course4 = courseDao.getByID(4);

        Student student1 = studentDao.getByID(1);
        Student student2 = studentDao.getByID(2);
        Student student3 = studentDao.getByID(3);
        Student student4 = studentDao.getByID(4);
        Student student5 = studentDao.getByID(5);

        program1.addCourse(course1);
        program1.addCourse(course2);
        program2.addCourse(course3);
        program2.addCourse(course4);

        program1.addStudent(student1);
        program1.addStudent(student2);
        program1.addStudent(student3);
        program2.addStudent(student4);
        program2.addStudent(student5);

        courseDao.update(course1);
        courseDao.update(course2);
        courseDao.update(course3);
        courseDao.update(course4);

        studentDao.update(student1);
        studentDao.update(student2);
        studentDao.update(student3);
        studentDao.update(student4);
        studentDao.update(student5);

        programDao.insert(program1);
        programDao.insert(program2);
    }

    public void truncateTable() {
        programDao.truncate();
    }

    // NEEDS UPDATE BELOW!!

    private void add() {
        System.out.println("\n1. Adding new Program!");

        System.out.println("\nProgram name:");
        String name = InputHandler.getStringInput();
        System.out.println("Start Date (Date format " + new Date(System.currentTimeMillis()) + "):");
        Date start_date = Date.valueOf(InputHandler.getStringInput());

        Program program = new Program(name, start_date);

        System.out.println("\nAdd Program to a Program? (Y/N)");
        if (InputHandler.getStringInput().equalsIgnoreCase("y")) {
            System.out.println("\nPlease enter ID of Program to add program to:");
            Program program = programDao.getByID(InputHandler.getIntegerInput());

            program.addProgram(program);
            programDao.update(program);
        }

        System.out.println("\nAdd a Teacher to this Program? (Y/N)");
        if (InputHandler.getStringInput().equalsIgnoreCase("y")) {
            System.out.println("\nPlease enter ID of Teacher to add:");
            Teacher teacher = teacherDao.getByID(InputHandler.getIntegerInput());

            program.addTeacher(teacher);
            teacherDao.update(teacher);
        }

        programDao.insert(program);

        System.out.println("\nNew Program " + program + " added successfully!");
    }

    private void update() {
        System.out.println("\n2. Updating Program information");

        System.out.println("\nPlease enter Program ID:");
        Program program = programDao.getByID(InputHandler.getIntegerInput());

        System.out.println("\n" + program + " SELECTED!");

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
                    3. Program
                    4. Teachers
                    0. Back to menu
                                        
                    Make your menu choice by writing the NUMBER and then press ENTER!
                    ↓ Write Here ↓""");
            choice = InputHandler.getIntegerInput();

            switch (choice) {
                case 1 -> updateName(program);
                case 2 -> updateStartDate(program);
                case 3 -> updateProgram(program);
                case 4 -> updateTeachers(program);
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
        System.out.println("Program current Start Date: " + program.getStart_date().toString());

        System.out.println("\nPlease enter new Start Date (Date format " + new Date(System.currentTimeMillis()) + "):");
        Date startDate = Date.valueOf(InputHandler.getStringInput());
        program.setStart_date(startDate);

        updatedPrint(program);
    }

    private void updateProgram(Program program) {
        System.out.println("\n3. Updating Program");
        System.out.println("Program current Program: " + program.getProgram().getName());

        System.out.println("\nPlease enter ID of new Program:");
        Program program = programDao.getByID(InputHandler.getIntegerInput());

        program.addProgram(program);
        programDao.update(program);

        updatedPrint(program);
    }

    private void updateTeachers(Program program) {
        System.out.println("\n4. Updating Teachers");
        System.out.println("\nProgram current Teachers:");
        program.getTeachers().forEach(System.out::println);

        System.out.println("\nPlease enter ID of Teacher:");
        Teacher teacher = teacherDao.getByID(InputHandler.getIntegerInput());

        System.out.println("\nDelete or Add selected Teacher? (D/A)");
        String input;

        do {
            input = InputHandler.getStringInput();
            if (input.equalsIgnoreCase("d")) {
                program.deleteTeacher(teacher);
                teacherDao.update(teacher);
                break;
            } else if (input.equalsIgnoreCase("a")) {
                program.addTeacher(teacher);
                teacherDao.update(teacher);
                break;
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
                    4. Included in Program
                    5. Taught by Teacher
                    0. Back to menu
                                        
                    Make your menu choice by writing the NUMBER and then press ENTER!
                    ↓ Write Here ↓""");
            choice = InputHandler.getIntegerInput();

            switch (choice) {
                case 1 -> getByID();
                case 2 -> getByName();
                case 3 -> getByStartDate();
                case 4 -> getByProgram();
                case 5 -> getByTeacher();
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

    private void getByProgram() {
        System.out.println("\nPlease enter ID of Program:");
        int id = InputHandler.getIntegerInput();
        Program program = programDao.getByID(id);

        System.out.println("\nResult:");
        programDao.getByProgram(program).forEach(System.out::println);
    }

    private void getByTeacher() {
        System.out.println("\nPlease enter ID of Teacher:");
        int id = InputHandler.getIntegerInput();
        Teacher teacher = teacherDao.getByID(id);

        System.out.println("\nResult:");
        programDao.getByTeacher(teacher).forEach(System.out::println);
    }

    private void showAll() {
        System.out.println("\n5. Show list of All Programs");
        allProgramsPrint();
    }

    private void allProgramsPrint() {
        programDao.getAll().forEach(System.out::println);
    }
}
