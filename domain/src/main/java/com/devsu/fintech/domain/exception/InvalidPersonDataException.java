package com.devsu.fintech.domain.exception;

/**
 * Thrown when a {@code Person} is built with data that violates its invariants
 * (required fields, valid age, mandatory catalog references).
 */
public class InvalidPersonDataException extends RuntimeException {

    public InvalidPersonDataException(String message) {
        super(message);
    }
}
