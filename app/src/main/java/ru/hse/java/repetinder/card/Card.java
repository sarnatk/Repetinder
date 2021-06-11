package ru.hse.java.repetinder.card;

public class Card {
    private String userId;
    private String fullname;
    private String profileImageUrl;

    public Card(String userId, String fullname, String profileImageUrl) {
        this.userId = userId;
        this.fullname = fullname;
        this.profileImageUrl = profileImageUrl;
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl() {
        this.profileImageUrl = profileImageUrl;
    }
}
