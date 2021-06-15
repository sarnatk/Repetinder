package ru.hse.java.repetinder.user;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Tutor extends UserRepetinder {
    public Tutor(String fullname, String username, String email, Subject subject) {
        super(fullname, username, email, subject);
        approvedStudents = new LinkedList<>();
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMinPrice() {
        return minPrice;
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

    private int minPrice;
    private final boolean isFree = true;
    private int rang;
    private final List<Student> approvedStudents;
}
