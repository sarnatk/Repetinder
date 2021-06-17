package ru.hse.java.repetinder.user;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Student extends UserRepetinder {
    public Student(String fullname, String username, String email, Subject subject, Integer price) {
        super(fullname, username, email, subject, price);
        approvedTutors = new LinkedList<>();
        skippedTutors = new LinkedList<>();
    }

    public void addTutorToApproved(Tutor tutor) {
        approvedTutors.add(tutor);
    }

    public void addAllToApproved(Collection<? extends Tutor> tutors) {
        approvedTutors.addAll(tutors);
    }

    public boolean isTutorApproved(Tutor tutor) {
        return approvedTutors.contains(tutor);
    }

    public List<Tutor> getApprovedTutors() {
        return approvedTutors;
    }

    public void addTutorToSkipped(Tutor tutor) {
        skippedTutors.add(tutor);
    }

    public void addAllToSkipped(Collection<? extends Tutor> tutors) {
        skippedTutors.addAll(tutors);
    }

    public boolean isTutorSkipped(Tutor tutor) {
        return skippedTutors.contains(tutor);
    }

    public List<Tutor> getSkippedTutors() {
        return skippedTutors;
    }

    private final List<Tutor> approvedTutors;
    private final List<Tutor> skippedTutors;
}

