package com.weintern.sms;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Centralized console input and validation.
 */
public class InputHelper {
    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    public String readOptional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(value);
                if (number >= min && number <= max) return number;
            } catch (NumberFormatException ignored) { }
            System.out.printf("Enter a whole number between %d and %d.%n", min, max);
        }
    }

    public double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                double number = Double.parseDouble(value);
                if (Double.isFinite(number) && number >= min && number <= max) return number;
            } catch (NumberFormatException ignored) { }
            System.out.printf("Enter a number between %.1f and %.1f.%n", min, max);
        }
    }

    public List<String> readSubjects(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            List<String> subjects = new ArrayList<>();
            if (!line.isEmpty()) {
                for (String part : line.split(",")) {
                    String subject = part.trim();
                    if (!subject.isEmpty() && !subjects.contains(subject)) subjects.add(subject);
                }
            }
            if (!subjects.isEmpty()) return subjects;
            System.out.println("Enter at least one subject, separated by commas.");
        }
    }

    public boolean confirm(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String answer = scanner.nextLine().trim();
            if (answer.equalsIgnoreCase("y")) return true;
            if (answer.equalsIgnoreCase("n")) return false;
            System.out.println("Please enter y or n.");
        }
    }
}
