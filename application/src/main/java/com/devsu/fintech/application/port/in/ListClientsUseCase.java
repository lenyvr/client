package com.devsu.fintech.application.port.in;

import com.devsu.fintech.domain.model.Client;

import java.util.List;

/**
 * Input port: list active clients with an optional case-insensitive filter
 * (name, identification number or client code).
 */
public interface ListClientsUseCase {

    List<Client> list(String filter);
}
