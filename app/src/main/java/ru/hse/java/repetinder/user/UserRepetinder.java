package ru.hse.java.repetinder.user;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

import ru.hse.java.repetinder.photo.SerializableBitmap;

public class UserRepetinder implements Serializable { //extends RealmObject {

    public UserRepetinder(String fullname, String username, String email, Subject subject) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.subject = subject;
    }

    public UserRepetinder() {
        this("fullname", "username", "mail.ru", Subject.MATH);
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
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

    public enum Subject implements Serializable {
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

    public String getUsername() {
        return username;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void changeOnlineStatus() {
        isOnline = !isOnline;
    }

    public Bitmap getBitmap() {
        if (bitmap == null) {
            return null;
        }
        return bitmap.getBitmap();
    }

    public void setBitmap(Bitmap newBitmap) {
        bitmap = new SerializableBitmap(newBitmap);
    }

    private final String username;
    private final String fullname;
    private GroupType groupType = GroupType.SINGLE;
    private boolean isOnline;
    private Subject subject;
    private final String email;
    private SerializableBitmap bitmap = null;

    private Date lastOnlineTime = new Date(System.currentTimeMillis());
}
