package com.devsu.fintech.application.port.in;

/**
 * Input port: deactivate a client (soft delete, sets status to false).
 */
public interface DeactivateClientUseCase {

    void deactivate(Long clientId);
}
