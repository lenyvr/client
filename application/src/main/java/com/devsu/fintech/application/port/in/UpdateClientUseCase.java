package com.devsu.fintech.application.port.in;

import com.devsu.fintech.domain.model.Client;

/**
 * Input port: update an existing client (implicitly updates its person data).
 */
public interface UpdateClientUseCase {

    Client update(Long clientId, UpdateClientCommand command);
}
