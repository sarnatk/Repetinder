package ru.hse.java.repetinder;

import java.util.ArrayList;
import java.util.Date;

public class User {

    public User(int id, String username, String password, UserRole userRole, GroupType groupType, ArrayList<Subject> subjects) {
                this.id = id;
                this.username = username;
                this.password = password;
                this.userRole = userRole;
                this.groupType = groupType;
                this.subjects.addAll(subjects);
    }

    public enum UserRole {
        STUDENT,
        TUTOR
    }

    public enum GroupType {
        SINGLE(1),
        SMALL_GROUP(5),
        MEDIUM_GROUP(15),
        BIG_GROUP(20);

        private final int quantity;

        GroupType(int quantity) {
            this.quantity = quantity;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public enum OnlineStatus {
        ONLINE,
        OFFLINE;

        private final Date lastOnlineTime = new Date(System.currentTimeMillis());
    }

    public enum Subject {
        MATH,
        CS,
        PHYSICS,
        CHEMISTRY,
        BIOLOGY,
        PSYCHOLOGY,
        ENGLISH,
        FRENCH,
        GERMAN,
        SPANISH,
        RUSSIAN,
        LITERATURE,
        DESIGN,
        ECONOMICS,
        LAW,
        MUSIC,
        HISTORY
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public int getId() {
        return id;
    }

    private int id;
    private String username;
    private String password;
    private UserRole userRole;
    private GroupType groupType;
    private OnlineStatus typeOnline;
    private ArrayList<Subject> subjects;
}
