package com.devsu.fintech.application.usecase;

import com.devsu.fintech.application.port.in.ListClientsUseCase;
import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.domain.port.out.ClientRepositorySPI;

import java.util.List;

/**
 * Lists active clients, optionally filtered (case-insensitive, partial) by name,
 * identification number or client code.
 */
public class ListClientsUseCaseImpl implements ListClientsUseCase {

    private final ClientRepositorySPI clientRepository;

    public ListClientsUseCaseImpl(ClientRepositorySPI clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> list(String filter) {
        return clientRepository.searchActive(filter);
    }
}
