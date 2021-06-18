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
        assertEquals("Success", UserRepetinder.validate(fullname, email, password));
    }

    @Test
    public void testValidationBadEmail() {
        String fullname = "Help";
        String email = "help.help";
        String password = "helpme";
        assertEquals("Email should contain '@' symbol", UserRepetinder.validate(fullname, email, password));
    }

    @Test
    public void testValidationBadPassword() {
        String fullname = "Help";
        String email = "help@help.help";
        String password = "help";
        assertEquals("Password should contain at least 6 symbols", UserRepetinder.validate(fullname, email, password));
    }

    @Test
    public void testValidationEmpty() {
        String fullname = "";
        String email = "help@help.help";
        String password = "helpme";
        assertEquals("You need to fill in each field", UserRepetinder.validate(fullname, email, password));
    }

    @Test
    public void testValidationMultiple() {
        String fullname = "";
        String email = "help.help";
        String password = "help";
        assertEquals("You need to fill in each field", UserRepetinder.validate(fullname, email, password));
    }
}
