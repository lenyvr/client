package com.devsu.fintech.domain.exception;

public class ClientHasOpenAccountsException extends RuntimeException {

    public ClientHasOpenAccountsException(Long clientId) {
        super("Client with id " + clientId + " has open accounts and cannot be deactivated");
    }
}
