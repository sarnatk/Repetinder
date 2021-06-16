package ru.hse.java.repetinder.user;

import java.io.Serializable;
import java.util.Date;

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

    public String getDate() {
        return dateOfBirth;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return aboutMe;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void changeOnlineStatus() {
        isOnline = !isOnline;
    }

   /* public Bitmap getBitmap() {
        if (bitmap == null) {
            return null;
        }
        return bitmap.getBitmap();
    }

    public void setBitmap(Bitmap newBitmap) {
        bitmap = new SerializableBitmap(newBitmap);
    }*/

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    private final String username;
    private final String fullname;
    private GroupType groupType = GroupType.SINGLE;
    private boolean isOnline;
    private Subject subject;
    private final String email;
    private String profileImageUrl;

    private String dateOfBirth;
    private String aboutMe;
    private String city;
    private Integer price = 0;

    private Date lastOnlineTime = new Date(System.currentTimeMillis());
}
