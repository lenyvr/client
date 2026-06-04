package com.devsu.fintech.application.port.in;

import com.devsu.fintech.domain.model.Client;

/**
 * Input port: create a client (persisting its person first).
 */
public interface CreateClientUseCase {

    Client create(Client client);
}
