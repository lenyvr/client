package com.devsu.fintech.domain.port.out;

import com.devsu.fintech.domain.model.Client;

import java.util.List;
import java.util.Optional;

/**
 * Output port for client persistence. Implementations live in the infrastructure layer
 * and are responsible for persisting the person before the client.
 */
public interface ClientRepositorySPI {

    /**
     * Finds a client by the identification number of its underlying person.
     */
    Optional<Client> findByIdentificationNumber(String identificationNumber);

    Optional<Client> findByClientId(Long clientId);

    /**
     * Persists (creates or updates) the client and its person, returning the stored state.
     */
    Client save(Client client);

    /**
     * Returns active clients. When {@code filter} is null or blank, all active clients are
     * returned; otherwise it matches (case-insensitive, partial) against first/last name,
     * identification number or client code.
     */
    List<Client> searchActive(String filter);
}
