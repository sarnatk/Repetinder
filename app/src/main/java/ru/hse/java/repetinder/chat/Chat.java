package ru.hse.java.repetinder.chat;

public class Chat {
    private String message;
    private boolean isCurrentUser;

    public Chat(String message, boolean isCurrentUser) {
        this.message = message;
        this.isCurrentUser = isCurrentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    public void setCurrentUser(boolean currentUser) {
        isCurrentUser = currentUser;
    }
}
