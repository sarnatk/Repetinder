package ru.hse.java.repetinder.chat;

import org.bson.types.ObjectId;

import java.util.List;

import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Chat { // extends RealmObject {
    @PrimaryKey
    private final ObjectId chatId = new ObjectId();
    @Required
    private ObjectId tutorId;
    @Required
    private List<ObjectId> studentsId;
    @Required
    private boolean tutorOnline;
    @Required
    private List<Boolean> studentsOnline;
    @Required
    private List<ChatMessage> messages;
}
