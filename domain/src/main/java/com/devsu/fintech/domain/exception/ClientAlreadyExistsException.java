package com.devsu.fintech.domain.exception;

/**
 * Thrown when attempting to create a client whose person identification number
 * already belongs to an active client.
 */
public class ClientAlreadyExistsException extends RuntimeException {

    public ClientAlreadyExistsException(String identificationNumber) {
        super("An active client already exists for identification number: " + identificationNumber);
    }
}
