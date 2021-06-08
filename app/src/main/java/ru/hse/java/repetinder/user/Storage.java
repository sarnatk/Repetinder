package ru.hse.java.repetinder.user;

import java.io.Serializable;

public class Storage implements Serializable {
    public UserRepetinder currentUser = new UserRepetinder();
    public String userRole = "1";
}
