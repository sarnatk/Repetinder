package ru.hse.java.repetinder.user;

import java.io.Serializable;

public class UserRepetinder implements Serializable {

    public UserRepetinder(String fullname, String username, String email, Subject subject, Integer price) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.subject = subject;
        this.price = price;
    }

    public UserRepetinder() {
        this("fullname", "username", "mail.ru", Subject.MATH, 0);
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
    private Subject subject;
    private final String email;
    private String profileImageUrl;

    private String dateOfBirth = "default";
    private String aboutMe = "default";
    private String city = "default";
    private Integer price;
}
