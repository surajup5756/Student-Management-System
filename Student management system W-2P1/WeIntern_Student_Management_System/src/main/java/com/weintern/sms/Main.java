package com.weintern.sms;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Application entry point and menu workflow.
 */
public class Main {
    private static final String LINE =
            "================================================================================";

    public static void main(String[] args) {
        Path dataFile = Paths.get("data", "students.dat");
        StudentManager manager = new StudentManager(dataFile);

        try (Scanner scanner = new Scanner(System.in)) {
            InputHelper input = new InputHelper(scanner);
            boolean running = true;

            while (running) {
                printMenu(manager);
                int choice = input.readInt("Choose an option: ", 0, 9);
                System.out.println();

                switch (choice) {
                    case 1: addStudent(input, manager); break;
                    case 2: displayStudents(manager.getAllStudents(), "ALL STUDENTS"); break;
                    case 3: searchStudent(input, manager); break;
                    case 4: updateStudent(input, manager); break;
                    case 5: deleteStudent(input, manager); break;
                    case 6:
                        System.out.printf("Class average GPA: %.2f / 10.00%n",
                                manager.calculateAverageGrade());
                        break;
                    case 7: displayStudents(manager.getStudentsSortedByName(), "STUDENTS SORTED BY NAME"); break;
                    case 8: exportCsv(input, manager); break;
                    case 9:
                        displayIdAllocation(manager);
                        break;
                    case 0:
                        running = false;
                        System.out.println("Thank you for using Student Management System. Goodbye!");
                        break;
                    default: // readInt already validates this range
                        break;
                }
                if (running) {
                    System.out.println();
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                }
            }
        }
    }

    private static void printMenu(StudentManager manager) {
        System.out.println(LINE);
        System.out.println("                     STUDENT MANAGEMENT SYSTEM");
        System.out.println(LINE);
        System.out.printf(" Total Students: %-5d | Class Average GPA: %.2f / 10.00%n",
                manager.getAllStudents().size(), manager.calculateAverageGrade());
        System.out.println(LINE);
        System.out.println(" 1. Add New Student");
        System.out.println(" 2. Display All Students");
        System.out.println(" 3. Search Student (ID / Name)");
        System.out.println(" 4. Update Student Details");
        System.out.println(" 5. Delete Student");
        System.out.println(" 6. Calculate Class Average");
        System.out.println(" 7. Sort Students by Name");
        System.out.println(" 8. Export Students to CSV");
        System.out.println(" 9. View ID Allocation / Vacant IDs");
        System.out.println(" 0. Exit");
        System.out.println(LINE);
    }

    private static void addStudent(InputHelper input, StudentManager manager) {
        System.out.println("--- ADD NEW STUDENT ---");
        List<Integer> vacantIds = manager.getVacantIds();

        if (!vacantIds.isEmpty()) {
            System.out.println("Previously allocated vacant IDs: " + vacantIds);
            System.out.println("1. Reuse a vacant ID");
            System.out.println("2. Enter a new ID");
            int idChoice = input.readInt("Choose ID option: ", 1, 2);
            int id;
            if (idChoice == 1) {
                System.out.println("Available IDs: " + vacantIds);
                while (true) {
                    id = input.readInt("Select a vacant Student ID: ", 1, 999999);
                    if (vacantIds.contains(id)) break;
                    System.out.println("That ID is not in the vacant ID list. Choose one of: " + vacantIds);
                }
            } else {
                id = input.readInt("New Student ID (1-999999): ", 1, 999999);
                if (manager.findById(id) != null || manager.getAllAllocatedIds().contains(id)) {
                    System.out.println("That ID is already allocated. Choose a vacant ID or a never-used ID.");
                    return;
                }
            }
            addStudentDetails(input, manager, id);
        } else {
            System.out.println("No vacant IDs are available.");
            int id = input.readInt("New Student ID (1-999999): ", 1, 999999);
            if (manager.getAllAllocatedIds().contains(id)) {
                System.out.println("That ID has already been allocated. Please choose a new ID.");
                return;
            }
            addStudentDetails(input, manager, id);
        }
    }

    private static void addStudentDetails(InputHelper input, StudentManager manager, int id) {
        String name = input.readNonEmpty("Full name: ");
        int age = input.readInt("Age (5-100): ", 5, 100);
        double grade = input.readDouble("Grade / GPA (0-10): ", 0, 10);
        List<String> subjects = input.readSubjects("Subjects (comma-separated): ");

        Student student = new Student(id, name, age, grade, subjects);
        if (manager.addStudent(student)) {
            System.out.println("Student added successfully with ID " + id + ".");
        } else {
            System.out.println("Could not add student. This ID is currently occupied.");
        }
    }

    private static void displayIdAllocation(StudentManager manager) {
        System.out.println("--- STUDENT ID ALLOCATION STATUS ---");
        List<Integer> ids = manager.getAllAllocatedIds();
        if (ids.isEmpty()) {
            System.out.println("No student IDs have been allocated yet.");
            return;
        }
        System.out.printf("%-12s %-24s %s%n", "STUDENT ID", "STUDENT NAME", "STATUS");
        System.out.println("------------------------------------------------------");
        for (Integer id : ids) {
            Student student = manager.findById(id);
            if (student == null) {
                System.out.printf("%-12d %-24s %s%n", id, "-", "VACANT");
            } else {
                System.out.printf("%-12d %-24s %s%n", id, student.getName(), "ACTIVE");
            }
        }
        System.out.println("Vacant IDs available for reuse: " + manager.getVacantIds());
    }

    private static void displayStudents(List<Student> students, String heading) {
        System.out.println("--- " + heading + " ---");
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        System.out.printf("%-8s %-22s %-6s %-8s %s%n",
                "ID", "NAME", "AGE", "GPA", "SUBJECTS");
        System.out.println(LINE);
        for (Student student : students) System.out.println(student);
        System.out.println(LINE);
        System.out.println("Records displayed: " + students.size());
    }

    private static void searchStudent(InputHelper input, StudentManager manager) {
        System.out.println("--- SEARCH STUDENT ---");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by name");
        int choice = input.readInt("Choose search type: ", 1, 2);
        if (choice == 1) {
            int id = input.readInt("Enter student ID: ", 1, 999999);
            Student student = manager.findById(id);
            if (student == null) System.out.println("Student not found.");
            else displayStudents(java.util.Collections.singletonList(student), "SEARCH RESULT");
        } else {
            String name = input.readNonEmpty("Enter full or partial name: ");
            displayStudents(manager.searchByName(name), "SEARCH RESULTS");
        }
    }

    private static void updateStudent(InputHelper input, StudentManager manager) {
        System.out.println("--- UPDATE STUDENT ---");
        int id = input.readInt("Enter student ID: ", 1, 999999);
        Student current = manager.findById(id);
        if (current == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println("Current record: " + current);
        System.out.println("Leave a field blank to keep its current value.");

        String nameText = input.readOptional("New name [" + current.getName() + "]: ");
        Integer age = readOptionalInt(input, current.getAge());
        Double grade = readOptionalDouble(input, current.getGrade());
        String subjectsText = input.readOptional("New subjects [" +
                String.join(", ", current.getSubjects()) + "]: ");
        List<String> subjects = null;
        if (!subjectsText.isEmpty()) {
            subjects = new java.util.ArrayList<>();
            for (String part : subjectsText.split(",")) {
                String value = part.trim();
                if (!value.isEmpty() && !subjects.contains(value)) subjects.add(value);
            }
            if (subjects.isEmpty()) {
                System.out.println("No valid subjects supplied; keeping existing subjects.");
                subjects = null;
            }
        }

        String name = nameText.isEmpty() ? null : nameText;
        manager.updateStudent(id, name, age, grade, subjects);
        System.out.println("Student details updated successfully.");
    }

    private static Integer readOptionalInt(InputHelper input, int current) {
        while (true) {
            String value = input.readOptional("New age [" + current + "]: ");
            if (value.isEmpty()) return null;
            try {
                int number = Integer.parseInt(value);
                if (number >= 5 && number <= 100) return number;
            } catch (NumberFormatException ignored) { }
            System.out.println("Age must be a whole number between 5 and 100, or blank to skip.");
        }
    }

    private static Double readOptionalDouble(InputHelper input, double current) {
        while (true) {
            String value = input.readOptional("New GPA [" + current + "]: ");
            if (value.isEmpty()) return null;
            try {
                double number = Double.parseDouble(value);
                if (Double.isFinite(number) && number >= 0 && number <= 10) return number;
            } catch (NumberFormatException ignored) { }
            System.out.println("GPA must be between 0 and 10, or blank to skip.");
        }
    }

    private static void deleteStudent(InputHelper input, StudentManager manager) {
        System.out.println("--- DELETE STUDENT ---");
        int id = input.readInt("Enter student ID: ", 1, 999999);
        Student student = manager.findById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println("Selected: " + student);
        if (input.confirm("Are you sure you want to delete this record?")) {
            manager.deleteStudent(id);
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private static void exportCsv(InputHelper input, StudentManager manager) {
        String fileName = input.readOptional("CSV file path [data/students.csv]: ");
        if (fileName.isEmpty()) fileName = "data/students.csv";
        try {
            manager.exportToCsv(Paths.get(fileName));
            System.out.println("CSV exported successfully to: " + Paths.get(fileName).toAbsolutePath());
        } catch (IOException ex) {
            System.out.println("CSV export failed: " + ex.getMessage());
        }
    }
}
