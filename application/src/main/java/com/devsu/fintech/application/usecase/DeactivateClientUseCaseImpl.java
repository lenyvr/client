package com.devsu.fintech.application.usecase;

import com.devsu.fintech.application.port.in.DeactivateClientUseCase;
import com.devsu.fintech.domain.exception.ClientNotFoundException;
import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.domain.port.out.ClientRepositorySPI;

/**
 * Deactivates a client (soft delete): sets status to false.
 */
public class DeactivateClientUseCaseImpl implements DeactivateClientUseCase {

    private final ClientRepositorySPI clientRepository;

    public DeactivateClientUseCaseImpl(ClientRepositorySPI clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void deactivate(Long clientId) {
        Client client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));
        client.deactivate();
        clientRepository.save(client);
    }
}
