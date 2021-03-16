package ru.hse.java.repetinder;

public class User {

    public enum UserRole {
        STUDENT,
        TUTOR
    }

    public enum TypeClass {
        SINGLE(1),
        SMALL_GROUP(5),
        MEDIUM_GROUP(15),
        BIG_GROUP(20);

        private final int quantity;

        TypeClass(int quantity) {
            this.quantity = quantity;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public enum TypeOnline {
        ONLINE,
        OFFLINE_STUDENT,
        OFFLINE_TUTOR
    }

    public enum Subject {
        MATH,
        CS,
        PHYSICS,
        CHEMISTRY,
        BIOLOGY,
        PSYCHOLOGY,
        ENGLISH,
        FRENCH,
        GERMAN,
        SPANISH,
        RUSSIAN,
        LITERATURE,
        DESIGN,
        ECONOMICS,
        LAW,
        MUSIC,
        HISTORY
    }

    private int id;
    private String username;
    private String password;
    private UserRole userRole;
    private TypeClass typeClass;
    private TypeOnline typeOnline;
    private Subject subject;
}
