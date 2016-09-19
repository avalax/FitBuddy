package de.avalax.fitbuddy.port.adapter.persistence.exception;

public class DatabaseResourceNotFoundException extends RuntimeException {
    public DatabaseResourceNotFoundException(Exception e) {
        super(e);
    }
}
