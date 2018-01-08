package de.avalax.fitbuddy.domain.model;

public abstract class ResourceException extends Exception {
    public ResourceException() {
        super();
    }

    public ResourceException(Throwable cause) {
        super(cause);
    }

    public ResourceException(String message) {
        super(message);
    }
}
