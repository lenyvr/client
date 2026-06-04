package com.devsu.fintech.domain.exception;

/**
 * Thrown when a client cannot be found for the given identifier.
 */
public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(Long clientId) {
        super("Client not found with id: " + clientId);
    }
}
