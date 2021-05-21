package ru.hse.java.repetinder.exception;

public class MatchingAlgorithmException extends Exception {
    public MatchingAlgorithmException() {
        super();
    }

    public MatchingAlgorithmException(String message) {
        super(message);
    }

    public MatchingAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatchingAlgorithmException(Throwable cause) {
        super(cause);
    }
}
