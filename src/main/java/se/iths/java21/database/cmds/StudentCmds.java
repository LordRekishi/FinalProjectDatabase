package se.iths.java21.database.cmds;

import se.iths.java21.database.dao.ProgramDao;
import se.iths.java21.database.dao.StudentDao;
import se.iths.java21.database.entities.Program;
import se.iths.java21.database.entities.Student;
import se.iths.java21.database.impl.ProgramDaoImpl;
import se.iths.java21.database.impl.StudentDaoImpl;
import se.iths.java21.database.tools.Command;
import se.iths.java21.database.tools.InputHandler;

import java.sql.Date;

public class StudentCmds {
    private final Command[] commands = new Command[6];
    private final StudentDao studentDao = new StudentDaoImpl();
    private final ProgramDao programDao = new ProgramDaoImpl();

    public StudentCmds() {
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
                STUDENTS     | 1. Add | | 2. Update | | 3. Delete | | 4. Filter | | 5. Show All | | 0. Exit |
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

    public void insertDemoStudents() {
        Student student1 = new Student("Patrik", "Fallqvist", Date.valueOf("1986-05-07"), true);
        Student student2 = new Student("Robert", "Fallqvist", Date.valueOf("1988-01-20"), false);
        Student student3 = new Student("Felix", "Jacobsen", Date.valueOf("1995-01-02"), true);
        Student student4 = new Student("Lum", "Shabani", Date.valueOf("1990-10-20"), true);
        Student student5 = new Student("Börje", "Salming", Date.valueOf("1965-03-07"), false);
        studentDao.insert(student1);
        studentDao.insert(student2);
        studentDao.insert(student3);
        studentDao.insert(student4);
        studentDao.insert(student5);
    }

    public void truncateTable() {
        studentDao.truncate();
    }

    private void add() {
        System.out.println("\n1. Adding new Student!");

        System.out.println("\nFirst name:");
        String firstName = InputHandler.getStringInput();
        System.out.println("\nLast name:");
        String lastName = InputHandler.getStringInput();
        System.out.println("\nDate of Birth (Date format " + new Date(System.currentTimeMillis()) + "):");
        Date date_of_birth = Date.valueOf(InputHandler.getStringInput());
        System.out.println("\nActive Student? (Y/N)");
        boolean active = InputHandler.getStringInput().equalsIgnoreCase("y");

        Student newStudent = new Student(firstName, lastName, date_of_birth, active);

        System.out.println("\nAdd student to a Program? (Y/N)");
        if (InputHandler.getStringInput().equalsIgnoreCase("y")) {
            System.out.println("\nPlease enter ID of Program to add student to:");
            Program program = programDao.getByID(InputHandler.getIntegerInput());

            program.addStudent(newStudent);
            programDao.update(program);
        }

        studentDao.insert(newStudent);

        System.out.println("\nNew Student " + newStudent + " added successfully!");
    }

    private void update() {
        System.out.println("\n2. Updating Student information");

        System.out.println("\nPlease enter Student ID:");
        Student student = studentDao.getByID(InputHandler.getIntegerInput());

        System.out.println("\n" + student + " SELECTED!");

        updateMenuChoice(student);

        studentDao.update(student);
    }

    private void updateMenuChoice(Student student) {
        int choice;

        do {
            System.out.println("""
                    What do you want to Update?
                                        
                    1. First name
                    2. Last name
                    3. Date of Birth
                    4. Student status
                    5. Program
                    0. Back to menu
                                        
                    Make your menu choice by writing the NUMBER and then press ENTER!
                    ↓ Write Here ↓""");
            choice = InputHandler.getIntegerInput();

            switch (choice) {
                case 1 -> updateFirstName(student);
                case 2 -> updateLastName(student);
                case 3 -> updateDOB(student);
                case 4 -> updateStatus(student);
                case 5 -> updateProgram(student);
                case 0 -> System.out.println("\nReturning to menu...");
                default -> System.out.println("\nInvalid choice, try again");
            }

        } while (choice != 0);
    }

    private void updateFirstName(Student student) {
        System.out.println("\n1. Updating First Name");
        System.out.println("Student current First Name: " + student.getFirst_name());

        System.out.println("\nPlease enter new First Name:");
        String name = InputHandler.getStringInput();
        student.setFirst_name(name);

        updatedPrint(student);
    }

    private void updateLastName(Student student) {
        System.out.println("\n2. Updating Last Name");
        System.out.println("Student current Last Name: " + student.getLast_name());

        System.out.println("\nPlease enter new Last Name:");
        String name = InputHandler.getStringInput();
        student.setLast_name(name);

        updatedPrint(student);
    }

    private void updateDOB(Student student) {
        System.out.println("\n3. Updating Date of Birth");
        System.out.println("Student current Date of Birth: " + student.getDate_of_birth().toString());

        System.out.println("\nPlease enter new Date of Birth (Date format " + new Date(System.currentTimeMillis()) + "):");
        Date dob = Date.valueOf(InputHandler.getStringInput());
        student.setDate_of_birth(dob);

        updatedPrint(student);
    }

    private void updateStatus(Student student) {
        System.out.println("\n4. Updating Student status");

        printCurrentStatus(student);
        enterNewStatus(student);

        updatedPrint(student);
    }

    private void printCurrentStatus(Student student) {
        String status;
        if (student.isActive()) {
            status = "Active";
        } else {
            status = "Not Active";
        }

        System.out.println("Student current status: " + status);
    }

    private void enterNewStatus(Student student) {
        System.out.println("\nPlease enter new Status (1. Active / 2. Not Active):");

        String input;

        do {
            input = InputHandler.getStringInput();

            if (input.equalsIgnoreCase("active") || input.equalsIgnoreCase("1")) {
                student.setActive(true);
                break;
            } else if (input.equalsIgnoreCase("not active") || input.equalsIgnoreCase("2")) {
                student.setActive(false);
                break;
            }

            System.out.println("Invalid Input... Should be: Active OR Not Active");
        } while (true);
    }

    private void updateProgram(Student student) {
        System.out.println("\n5. Updating Program");
        System.out.println("Student current Program: " + student.getProgram().getName());

        System.out.println("\nPlease enter ID of new Program:");
        Program program = programDao.getByID(InputHandler.getIntegerInput());

        program.addStudent(student);
        programDao.update(program);

        updatedPrint(student);
    }

    private void updatedPrint(Student student) {
        System.out.println("\nStudent was updated successfully!");
        System.out.println("Updated info: " + student);

        System.out.println("\nReturning to menu...");
    }

    private void delete() {
        deletePrint();
        int id = InputHandler.getIntegerInput();

        if (id == 0) {
            System.out.println("\nCancelling and returning to menu...");
            return;
        }

        Student studentToDelete = studentDao.getByID(id);
        studentDao.delete(studentToDelete);

        deleteExitPrint();
    }

    private void deletePrint() {
        System.out.println("\n3. Deleting Student");

        System.out.println("\nList of All Students:");
        allStudentsPrint();

        System.out.println("""
                Please enter ID of Student to Delete:
                                
                !! WARNING THIS IS PERMANENT !!
                !! Write 0 to cancel !!
                                
                ↓ Write Here ↓""");
    }

    private void deleteExitPrint() {
        System.out.println("\nStudent Deleted successfully!");

        System.out.println("\nNew List of All Students:");
        allStudentsPrint();

        System.out.println("\nReturning to menu...");
    }

    private void filter() {
        System.out.println("\n4. Show Filtered list of Students");

        int choice;

        do {
            System.out.println("""
                    Filter students by what?
                                        
                    1. ID
                    2. Name
                    3. Date of Birth
                    4. Student status
                    5. Program
                    0. Back to menu
                                        
                    Make your menu choice by writing the NUMBER and then press ENTER!
                    ↓ Write Here ↓""");
            choice = InputHandler.getIntegerInput();

            switch (choice) {
                case 1 -> getByID();
                case 2 -> getByName();
                case 3 -> getByDOB();
                case 4 -> getByStatus();
                case 5 -> getByProgram();
                case 0 -> System.out.println("\nReturning to menu...");
                default -> System.out.println("\nInvalid choice, try again");
            }

        } while (choice != 0);

    }

    private void getByID() {
        System.out.println("\nPlease enter ID:");
        int id = InputHandler.getIntegerInput();

        System.out.println("\nResult:");
        System.out.println(studentDao.getByID(id));
    }

    private void getByName() {
        System.out.println("\nPlease enter whole or part of a Name:");
        String name = InputHandler.getStringInput();

        System.out.println("\nResult:");
        studentDao.getByName(name).forEach(System.out::println);
    }

    private void getByDOB() {
        System.out.println("\nPlease enter range of Date of Birth (Date format " + new Date(System.currentTimeMillis()) + "):");

        System.out.println("\nMinimum Date:");
        Date minDOB = Date.valueOf(InputHandler.getStringInput());
        System.out.println("Maximum Date:");
        Date maxDOB = Date.valueOf(InputHandler.getStringInput());

        System.out.println("\nResult:");
        studentDao.getByDateOfBirth(minDOB, maxDOB).forEach(System.out::println);
    }

    private void getByStatus() {
        System.out.println("\nPlease enter either ACTIVE or NOT ACTIVE:");

        String input;

        do {
            input = InputHandler.getStringInput();

            if (input.equalsIgnoreCase("active")) {
                System.out.println("\nResult:");
                studentDao.getByIsActive(true).forEach(System.out::println);
                break;
            } else if (input.equalsIgnoreCase("not active")) {
                System.out.println("\nResult:");
                studentDao.getByIsActive(false).forEach(System.out::println);
                break;
            } else {
                System.out.println("\nInvalid input, try again...");
            }
        } while (true);

    }

    private void getByProgram() {
        System.out.println("\nPlease enter ID of Program:");
        int id = InputHandler.getIntegerInput();
        Program program = programDao.getByID(id);

        System.out.println("\nResult:");
        studentDao.getByProgram(program).forEach(System.out::println);
    }

    private void showAll() {
        System.out.println("\n5. Show list of All Students");
        allStudentsPrint();
    }

    private void allStudentsPrint() {
        studentDao.getAll().forEach(System.out::println);
    }
}
