package se.iths.java21.database.tools;

import java.util.Scanner;

public class InputHandler {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getIntegerInput() {
        String input;

        do {
            input = scanner.nextLine();
            if (!isNumericInteger(input)) {
                System.out.println("""
                        Try Again!
                        ↓ Write Here ↓""");
            }
        } while (!isNumericInteger(input));
        return Integer.parseInt(input);
    }

    public static float getFloatInput() {
        String input;

        do {
            input = scanner.nextLine();
            if (!isNumericFloat(input)) {
                System.out.println("""
                        Try Again!
                        ↓ Write Here ↓""");
            }
        } while (!isNumericFloat(input));
        return Float.parseFloat(input);
    }

    public static String getStringInput() {
        String input;

        do {
            input = scanner.nextLine();
            if (input == null) {
                System.out.println("""
                        Try Again!
                        ↓ Write Here ↓""");
            }
        } while (input == null);
        return input;
    }

    public static boolean isNumericInteger(String searchInput) {
        if (searchInput == null)
            return false;

        try {
            Integer.parseInt(searchInput);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNumericFloat(String searchInput) {
        if (searchInput == null)
            return false;

        try {
            Float.parseFloat(searchInput);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
