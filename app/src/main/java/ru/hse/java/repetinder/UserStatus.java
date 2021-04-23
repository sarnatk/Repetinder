package ru.hse.java.repetinder;

public enum UserStatus {
    Open("Open"),
    InProgress("In Progress"),
    Complete("Complete");
    String displayName;
    UserStatus(String displayName) {
        this.displayName = displayName;
    }
}