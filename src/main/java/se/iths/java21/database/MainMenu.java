package se.iths.java21.database;

import se.iths.java21.database.cmds.*;
import se.iths.java21.database.tools.*;

public class MainMenu {
    private final Command[] commands = new Command[5];

    public MainMenu(StudentCmds studentCmds, TeacherCmds teacherCmds, CourseCmds courseCmds, ProgramCmds programCmds) {
        commands[1] = studentCmds::run;
        commands[2] = teacherCmds::run;
        commands[3] = courseCmds::run;
        commands[4] = programCmds::run;
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
                                
                             +-------------+ +-------------+ +------------+ +-------------+ +---------+
                SCHOOL ADMIN | 1. Students | | 2. Teachers | | 3. Courses | | 4. Programs | | 0. Exit |
                             +-------------+ +-------------+ +------------+ +-------------+ +---------+
                                
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
        System.out.println("\nExiting Program...");
        System.exit(0);
    }
}
