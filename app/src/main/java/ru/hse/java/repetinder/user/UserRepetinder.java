package ru.hse.java.repetinder.user;

import android.widget.Toast;

import java.io.Serializable;

import ru.hse.java.repetinder.activities.RegisterActivity;

public class UserRepetinder implements Serializable {

    public UserRepetinder(String fullname, String email, Subject subject, Integer price) {
        this.fullname = fullname;
        this.email = email;
        this.subject = subject;
        this.price = price;
    }

    public UserRepetinder() {
        this("fullname", "mail.ru", Subject.MATH, 0);
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCity() {
        return city;
    }

    public String getAboutMe() {
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

    public static String validate(String fullname, String email, String password, String subject) {
        if (email.isEmpty() || password.isEmpty() || fullname.isEmpty()) {
            return "You need to fill in each field";
        } else if (!email.contains("@")) {
            return "Email should contain '@' symbol";
        } else if (password.length() < 6) {
            return "Password should contain at least 6 symbols";
        } else if (subject.equals("Select subject")) {
            return "You have to select subject";
        }
        return "Success";
    }

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
