package ru.hse.java.repetinder.user;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Tutor extends UserRepetinder {
    public Tutor(String fullname, String username, String email, Subject subject) {
        super(fullname, username, email, subject);
        approvedStudents = new LinkedList<>();
    }

    public int getRang() {
        return rang;
    }

    public void increaseRang() {
        rang++;
    }

    public void decreaseRang() {
        rang--;
    }

    public void addStudentToList(Student student) {
        approvedStudents.add(student);
    }

    public void addAll(Collection<? extends Student> students) {
        approvedStudents.addAll(students);
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    private boolean isSeen = true;
    private int rang;
    private final List<Student> approvedStudents;


}
