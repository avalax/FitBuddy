package de.avalax.fitbuddy.exception;

public class DialogListenerException extends RuntimeException {
    public DialogListenerException(String message, Exception exception) {
        super(message, exception);
    }
}
