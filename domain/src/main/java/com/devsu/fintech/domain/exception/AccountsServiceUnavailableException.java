package com.devsu.fintech.domain.exception;

public class AccountsServiceUnavailableException extends RuntimeException {

    public AccountsServiceUnavailableException(Long clientId) {
        super("The service accounts does not respond for client_id " + clientId + ". is not possible verify if there are open accounts");
    }
}