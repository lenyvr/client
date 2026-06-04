package com.devsu.fintech.domain.exception;

/**
 * Thrown when a {@code Client} is built with data that violates its invariants
 * (required client code or password).
 */
public class InvalidClientDataException extends RuntimeException {

    public InvalidClientDataException(String message) {
        super(message);
    }
}
