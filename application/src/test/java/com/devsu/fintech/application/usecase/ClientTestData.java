package com.devsu.fintech.application.usecase;

import com.devsu.fintech.domain.model.Client;

/**
 * Test fixtures for the use case unit tests.
 */
final class ClientTestData {

    private ClientTestData() {
    }

    static Client client(Long clientId, Long personId, String identification, String password, boolean status) {
        return new Client(personId, "Jose", "Lema", 34, 2, identification,
                "Otavalo sn", "37890", "098254785", 1, "jose@mail.com",
                clientId, "C1", password, status);
    }

    static Client newRequest(String identification, String rawPassword) {
        return client(null, null, identification, rawPassword, true);
    }
}
