package com.devsu.fintech.application.usecase;

import com.devsu.fintech.application.port.in.UpdateClientCommand;
import com.devsu.fintech.application.port.in.UpdateClientUseCase;
import com.devsu.fintech.domain.exception.ClientNotFoundException;
import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.domain.port.out.ClientRepositorySPI;
import com.devsu.fintech.domain.port.out.PasswordEncoderSPI;

/**
 * Updates an existing client, implicitly updating its person data. The password is
 * re-encoded only when a non-blank value is provided; otherwise the stored one is kept.
 * The current status is preserved (deactivation has its own use case).
 */
public class UpdateClientUseCaseImpl implements UpdateClientUseCase {

    private final ClientRepositorySPI clientRepository;
    private final PasswordEncoderSPI passwordEncoder;

    public UpdateClientUseCaseImpl(ClientRepositorySPI clientRepository, PasswordEncoderSPI passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Client update(Long clientId, UpdateClientCommand command) {
        Client current = validateClientExistAndIsActive(clientId);

        String password = (command.password() == null || command.password().isBlank())
                ? current.getPassword()
                : passwordEncoder.encode(command.password());

        Client updated = new Client(
                current.getPersonId(),
                command.firstName(),
                command.lastName(),
                command.age(),
                command.genderId(),
                command.identificationNumber(),
                command.address(),
                command.postalCode(),
                command.contactNumber(),
                command.identificationTypeId(),
                command.email(),
                current.getClientId(),
                command.clientCode(),
                password,
                current.getStatus()
        );
        return clientRepository.save(updated);
    }

    private Client validateClientExistAndIsActive(Long clientId) {
        Client current = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        if (!current.isActive()) {
            throw new ClientNotFoundException(current.getClientId());
        }

        return  current;
    }
}
