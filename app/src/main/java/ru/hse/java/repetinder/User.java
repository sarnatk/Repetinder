package ru.hse.java.repetinder;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class User {

    public User(/*int id*/ String username, String password) {
               // this.id = id;
               // TODO: counting id?
                this.username = username;
                this.password = password;
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


  /*  public int getId() {
        return id;
    }
*/
    public String getUsername() {
        return username;
    }

    private String getPassword() {
        return password;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void changeOnlineStatus() {
        isOnline = !isOnline;
    }

 //   private final int id;
    private final String username;
    private final String password;
    private GroupType groupType;
    private boolean isOnline;
    private Subject subject;

    private final Date lastOnlineTime = new Date(System.currentTimeMillis());
}
