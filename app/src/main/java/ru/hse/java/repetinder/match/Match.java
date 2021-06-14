package ru.hse.java.repetinder.match;

public class Match {
    private String userId;
    private String fullname;
    private String email;
    private String profileImageUrl;
    private String userRole;

    public Match(String userId, String fullname, String email, String profileImageUrl, String userRole) {
        this.userId = userId;
        this.fullname = fullname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.userRole = userRole;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
