package de.avalax.fitbuddy.port.adapter.persistence.exception;

public class DatabaseResourceNotFoundException extends RuntimeException {
    public DatabaseResourceNotFoundException(Exception exception) {
        super(exception);
    }
}
