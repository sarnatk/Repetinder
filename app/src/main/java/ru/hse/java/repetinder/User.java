package ru.hse.java.repetinder;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import org.bson.types.ObjectId;

public class User { // extends RealmObject {

    public User(/*int id*/ String username, String password) {
               // this.id = id;
               // TODO: counting id?
                this.username = username;
                this.password = password;
    }

    public User() {
        this("name", "uiop");
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


    public ObjectId getId() {
        return _id;
    }
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

    @PrimaryKey
    private final ObjectId _id = new ObjectId();
    @Required
    private final String username;
    @Required
    private final String password;
    @Required
    private GroupType groupType;
    private boolean isOnline;
    @Required
    private Subject subject;

    private final Date lastOnlineTime = new Date(System.currentTimeMillis());
}
