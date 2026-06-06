package com.devsu.fintech.application.port.in;

public interface VerifyClientUseCase {

    boolean existsActiveClient(Long clientId);
}
