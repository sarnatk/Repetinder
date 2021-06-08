package ru.hse.java.repetinder;

public class Card {
    private String userId;
    private String fullname;

    public Card(String userId, String fullname) {
        this.userId = userId;
        this.fullname = fullname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
