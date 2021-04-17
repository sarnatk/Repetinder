package ru.hse.java.repetinder;

import java.util.ArrayList;
import java.util.Collection;

public class Student extends User {
    public Student(String username, String password) {
        super(username, password);
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinPrice() {
        return maxPrice;
    }

    public void addTutorToList(int id) {
        approvedTutors.add(id);
    }

    public void addAll(Collection<? extends Integer> ids) {
        approvedTutors.addAll(ids);
    }

    public boolean isTutorApproved(int id) {
        return approvedTutors.contains(id);
    }

    public ArrayList<Integer> getApprovedTutors() {
        return approvedTutors;
    }

    private int maxPrice;
    private ArrayList<Integer> approvedTutors;
}

