package com.weintern.sms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Persistent snapshot of active student records and all allocated ID slots.
 * The ID set includes both occupied and vacant IDs.
 */
public class StudentStore implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Student> students;
    // Kept as reservedIds for compatibility with the previous project ZIP.
    // In this version, these are reusable allocated ID slots, not permanently blocked IDs.
    private final Set<Integer> reservedIds;

    public StudentStore(List<Student> students, Set<Integer> allocatedIds) {
        this.students = new ArrayList<>(students);
        this.reservedIds = new HashSet<>(allocatedIds);
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public Set<Integer> getReservedIds() {
        return new HashSet<>(reservedIds);
    }
}
