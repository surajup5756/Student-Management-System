package com.weintern.sms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents one student record.
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private String name;
    private int age;
    private double grade;
    private List<String> subjects;

    public Student(int id, String name, int age, double grade, List<String> subjects) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.subjects = new ArrayList<>(subjects);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getGrade() { return grade; }
    public List<String> getSubjects() {
        return Collections.unmodifiableList(subjects);
    }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGrade(double grade) { this.grade = grade; }
    public void setSubjects(List<String> subjects) {
        this.subjects = new ArrayList<>(subjects);
    }

    @Override
    public String toString() {
        return String.format("%-8d %-22s %-6d %-8.2f %s",
                id, name, age, grade, String.join(", ", subjects));
    }
}
