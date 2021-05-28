package ru.hse.java.repetinder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Tutor extends UserRepetinder {
    public Tutor(int id, String fullname, String username, Subject subject) {
        super(id, fullname, username, subject);
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
    private int rang;
    private final List<Student> approvedStudents;
}
