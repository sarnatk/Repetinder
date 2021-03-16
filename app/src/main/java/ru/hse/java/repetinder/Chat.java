package ru.hse.java.repetinder;

import java.util.List;

public class Chat {
    private int chatID;
    private int tutorID;
    private List<Integer> studentsID;
    private boolean tutorOnline;
    private List<Boolean> studentsOnline;
    private List<ChatMessage> messages;
}
