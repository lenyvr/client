package com.devsu.fintech.application.usecase;

import com.devsu.fintech.application.port.in.VerifyClientUseCase;
import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.domain.port.out.ClientRepositorySPI;

public class VerifyClientUseCaseImpl implements VerifyClientUseCase {

    private final ClientRepositorySPI clientRepository;

    public VerifyClientUseCaseImpl(ClientRepositorySPI clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean existsActiveClient(Long clientId) {
        return clientRepository.findByClientId(clientId)
                .filter(Client::isActive)
                .isPresent();
    }
}
