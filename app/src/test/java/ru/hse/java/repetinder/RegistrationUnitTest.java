package ru.hse.java.repetinder;

import org.junit.Test;

import ru.hse.java.repetinder.user.UserRepetinder;

import static org.junit.Assert.assertEquals;

public class RegistrationUnitTest {
    @Test
    public void testValidationGood() {
        String fullname = "Help";
        String email = "help@help.help";
        String password = "helpme";
        String subject = "Math";
        assertEquals("Success", UserRepetinder.validate(fullname, email, password, subject));
    }

    @Test
    public void testValidationBadEmail() {
        String fullname = "Help";
        String email = "help.help";
        String password = "helpme";
        String subject = "Math";
        assertEquals("Email should contain '@' symbol", UserRepetinder.validate(fullname, email, password, subject));
    }

    @Test
    public void testValidationBadPassword() {
        String fullname = "Help";
        String email = "help@help.help";
        String password = "help";
        String subject = "Math";
        assertEquals("Password should contain at least 6 symbols", UserRepetinder.validate(fullname, email, password, subject));
    }

    @Test
    public void testValidationEmpty() {
        String fullname = "";
        String email = "help@help.help";
        String password = "helpme";
        String subject = "Math";
        assertEquals("You need to fill in each field", UserRepetinder.validate(fullname, email, password, subject));
    }

    @Test
    public void testValidationBadSubject() {
        String fullname = "Help";
        String email = "help@help.help";
        String password = "helpme";
        String subject = "Select subject";
        assertEquals("You have to select subject", UserRepetinder.validate(fullname, email, password, subject));
    }

    @Test
    public void testValidationMultiple() {
        String fullname = "";
        String email = "help.help";
        String password = "help";
        String subject = "Select subject";
        assertEquals("You need to fill in each field", UserRepetinder.validate(fullname, email, password, subject));
    }
}
