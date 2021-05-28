package ru.hse.java.repetinder;

import java.io.Serializable;
import java.util.Date;

public class UserRepetinder implements Serializable { //extends RealmObject {

    public UserRepetinder(int id, String fullname, String username, Subject subject) {
                this.id = id;
                this.fullname = fullname;
                this.username = username;
                this.subject = subject;
    }

    public UserRepetinder() {
        this(1, "fullname", "username", Subject.MATH);
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

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public Subject getSubject() {
        return subject;
    }


    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void changeOnlineStatus() {
        isOnline = !isOnline;
    }

    private final Integer id;
    private final String username;
    private final String fullname;
    private GroupType groupType;
    private boolean isOnline;
    private Subject subject;

    private Date lastOnlineTime = new Date(System.currentTimeMillis());
}
