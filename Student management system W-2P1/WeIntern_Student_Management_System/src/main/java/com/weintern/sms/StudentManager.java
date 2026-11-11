package com.weintern.sms;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Handles student CRUD operations and persistent ID-slot management.
 * A deleted student's ID becomes vacant and can be assigned again.
 */
public class StudentManager {
    private final List<Student> students = new ArrayList<>();
    private final Map<Integer, Student> studentsById = new HashMap<>();
    // Every ID that has ever been allocated is retained so vacant slots remain visible.
    private final Set<Integer> allocatedIds = new TreeSet<>();
    private final Path dataFile;

    public StudentManager(Path dataFile) {
        this.dataFile = dataFile;
        load();
    }

    /**
     * Adds a student into a never-used ID or a previously deleted vacant ID.
     */
    public boolean addStudent(Student student) {
        int id = student.getId();
        if (studentsById.containsKey(id)) return false;
        students.add(student);
        studentsById.put(id, student);
        allocatedIds.add(id);
        save();
        return true;
    }

    public List<Student> getAllStudents() {
        List<Student> result = new ArrayList<>(students);
        result.sort(Comparator.comparingInt(Student::getId));
        return result;
    }

    /**
     * Returns every ID ever allocated, in ascending order, including vacant slots.
     */
    public List<Integer> getAllAllocatedIds() {
        return new ArrayList<>(allocatedIds);
    }

    /**
     * Returns IDs that have a slot but currently have no active student.
     */
    public List<Integer> getVacantIds() {
        List<Integer> vacant = new ArrayList<>();
        for (Integer id : allocatedIds) {
            if (!studentsById.containsKey(id)) vacant.add(id);
        }
        return vacant;
    }

    public Student findById(int id) {
        return studentsById.get(id);
    }

    public List<Student> searchByName(String name) {
        String query = name.trim().toLowerCase(Locale.ROOT);
        List<Student> matches = new ArrayList<>();
        for (Student student : students) {
            if (student.getName().toLowerCase(Locale.ROOT).contains(query)) {
                matches.add(student);
            }
        }
        return matches;
    }

    public boolean updateStudent(int id, String name, Integer age,
                                 Double grade, List<String> subjects) {
        Student student = findById(id);
        if (student == null) return false;
        if (name != null) student.setName(name);
        if (age != null) student.setAge(age);
        if (grade != null) student.setGrade(grade);
        if (subjects != null) student.setSubjects(subjects);
        save();
        return true;
    }

    /**
     * Removes the active student. The ID remains in allocatedIds as a vacant slot.
     */
    public boolean deleteStudent(int id) {
        Student student = studentsById.remove(id);
        if (student == null) return false;
        students.remove(student);
        save();
        return true;
    }

    public double calculateAverageGrade() {
        if (students.isEmpty()) return 0.0;
        double total = 0;
        for (Student student : students) total += student.getGrade();
        return total / students.size();
    }

    public List<Student> getStudentsSortedByName() {
        List<Student> sorted = getAllStudents();
        sorted.sort(Comparator.comparing(
                Student::getName, String.CASE_INSENSITIVE_ORDER));
        return sorted;
    }

    public void exportToCsv(Path outputFile) throws IOException {
        Path parent = outputFile.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            writer.write("ID,Name,Age,Grade,Subjects");
            writer.newLine();
            for (Student s : getAllStudents()) {
                writer.write(s.getId() + "," + csv(s.getName()) + ","
                        + s.getAge() + "," + s.getGrade() + ","
                        + csv(String.join("; ", s.getSubjects())));
                writer.newLine();
            }
        }
    }

    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private void load() {
        if (!Files.exists(dataFile)) return;
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(dataFile)))) {
            Object stored = in.readObject();

            if (stored instanceof StudentStore) {
                StudentStore store = (StudentStore) stored;
                // The store's ID set is treated as the historical set of ID slots.
                allocatedIds.addAll(store.getReservedIds());
                for (Student student : store.getStudents()) {
                    if (!studentsById.containsKey(student.getId())) {
                        students.add(student);
                        studentsById.put(student.getId(), student);
                        allocatedIds.add(student.getId());
                    }
                }
            } else if (stored instanceof List<?>) {
                // Compatibility with the original version that saved List<Student>.
                for (Object item : (List<?>) stored) {
                    if (item instanceof Student) {
                        Student student = (Student) item;
                        if (!studentsById.containsKey(student.getId())) {
                            students.add(student);
                            studentsById.put(student.getId(), student);
                            allocatedIds.add(student.getId());
                        }
                    }
                }
                save();
            }
            // Persist the migrated/current ID-slot model in the new format.
            save();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Warning: Could not load saved data: " + ex.getMessage());
        }
    }

    private void save() {
        try {
            Path parent = dataFile.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            StudentStore store = new StudentStore(students, allocatedIds);
            try (ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(Files.newOutputStream(dataFile)))) {
                out.writeObject(store);
            }
        } catch (IOException ex) {
            System.out.println("Warning: Could not save data: " + ex.getMessage());
        }
    }
}
