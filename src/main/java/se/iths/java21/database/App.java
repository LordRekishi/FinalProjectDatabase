package se.iths.java21.database;

import se.iths.java21.database.cmds.CourseCmds;
import se.iths.java21.database.cmds.ProgramCmds;
import se.iths.java21.database.cmds.StudentCmds;
import se.iths.java21.database.cmds.TeacherCmds;
import se.iths.java21.database.tools.InputHandler;

public class App {
    private static final StudentCmds studentCmds = new StudentCmds();
    private static final TeacherCmds teacherCmds = new TeacherCmds();
    private static final CourseCmds courseCmds = new CourseCmds();
    private static final ProgramCmds programCmds = new ProgramCmds();
    private static final MainMenu mainMenu = new MainMenu(studentCmds, teacherCmds, courseCmds, programCmds);

    public static void main(String[] args) {
        startUp();
    }

    private static void startUp() {
        System.out.println("""
                Welcome to The Final Database Project
                By: Patrik Fallqvist Magnusson
                                
                INSTRUCTIONS:
                1. Connect to your LOCALHOST server using Port 3306
                2. Change the USER and PASSWORD in the src/main/resources/META-INF/persistence.xml file
                3. Create a database called FinalProject in MySQL Workbench
                GOOD WORK!
                                
                Preform First-time Setup (Y/N)""");

        if (InputHandler.getStringInput().equalsIgnoreCase("y")) {
            System.out.println("\nFirst-time Setup started...");
            insertDemoData();
            System.out.println("First-time Setup completed!");
        }

        System.out.println("\nReset Database? (Y/N)");

        if (InputHandler.getStringInput().equalsIgnoreCase("y")) {
            System.out.println("\nCleaning up and resetting database...");
            truncateData();
            insertDemoData();
            System.out.println("Reset completed!");
        }

        mainMenu.run();
    }

    private static void truncateData() {
        studentCmds.truncateTable();
        teacherCmds.truncateTable();
        courseCmds.truncateTable();
        programCmds.truncateTable();
    }

    private static void insertDemoData() {
        studentCmds.insertDemoStudents();
        teacherCmds.insertDemoTeachers();
        courseCmds.insertDemoCourses();
        programCmds.insertDemoPrograms();
    }

}
