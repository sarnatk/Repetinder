package ru.hse.java.repetinder.chat;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Chat {
    private String message;
    private Map<String, Long> time;
    private boolean isCurrentUser;

    public Chat(String message, Map<String, Long> time, boolean isCurrentUser) {
        this.message = message;
        this.time = time;
        this.isCurrentUser = isCurrentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeString() {
        Date today = Calendar.getInstance().getTime();
        String returningString = "";
        if (today.getYear() == time.get("year") &&
        today.getMonth() == time.get("month")) {
            if (today.getDate() == time.get("date")) {
                returningString += "today, ";
            } else if (today.getDate() == time.get("date") + 1) {
                returningString += "yesterday, ";
            } else {
                returningString += time.get("date") + "." + time.get("month") + ", ";
            }
        } else {
            long year = 1900 + time.get("year");
            returningString += time.get("date") + "." + time.get("month") + "." + year + ", ";
        }
        returningString += time.get("hours") + ":" + time.get("minutes");
        return returningString;
    }

    public void setTime(Map<String, Long> time) {
        this.time = time;
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    public void setCurrentUser(boolean currentUser) {
        isCurrentUser = currentUser;
    }
}
