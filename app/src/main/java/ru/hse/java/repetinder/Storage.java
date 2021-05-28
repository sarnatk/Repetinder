package ru.hse.java.repetinder;

import java.io.Serializable;

public class Storage implements Serializable {
    public final String appId = "repetinder-xlfqn";
    public String email;
    public UserRepetinder currentUser = new UserRepetinder();
    public String userRole;
}
