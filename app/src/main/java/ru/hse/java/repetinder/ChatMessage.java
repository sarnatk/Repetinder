package ru.hse.java.repetinder;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ChatMessage { // extends RealmObject {
    @PrimaryKey
    private final ObjectId chatMessageId = new ObjectId();
    @Required
    private LocalDateTime timeOfSending;
    @Required
    private ObjectId userID;
    @Required
    private String data;
}
