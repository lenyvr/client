package com.devsu.fintech.application.usecase;

import com.devsu.fintech.application.port.in.CreateClientUseCase;
import com.devsu.fintech.domain.exception.ClientAlreadyExistsException;
import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.domain.port.out.ClientRepositorySPI;
import com.devsu.fintech.domain.port.out.PasswordEncoderSPI;

import java.util.Optional;

/**
 * Creates a client. Business rules:
 * <ul>
 *     <li>If a client already exists for the same person identification number and is
 *     <b>active</b> → {@link ClientAlreadyExistsException}.</li>
 *     <li>If it exists but is <b>inactive</b> → it is updated with the new data and reactivated.</li>
 *     <li>Otherwise a brand new person + client is created.</li>
 * </ul>
 * The password is always stored encoded.
 */
public class CreateClientUseCaseImpl implements CreateClientUseCase {

    private final ClientRepositorySPI clientRepository;
    private final PasswordEncoderSPI passwordEncoder;

    public CreateClientUseCaseImpl(ClientRepositorySPI clientRepository, PasswordEncoderSPI passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Client create(Client client) {
        Optional<Client> existing = clientRepository.findByIdentificationNumber(client.getIdentificationNumber());

        if (existing.isPresent()) {
            Client current = existing.get();
            if (current.isActive()) {
                throw new ClientAlreadyExistsException(client.getIdentificationNumber());
            }
            // Inactive client: rebuild with the new data, keep existing ids, reactivate.
            Client reactivated = copyWithIds(client, current.getPersonId(), current.getClientId());
            reactivated.activate();
            reactivated.setPassword(passwordEncoder.encode(client.getPassword()));
            return clientRepository.save(reactivated);
        }

        Client toCreate = copyWithIds(client, null, null);
        toCreate.activate();
        toCreate.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.save(toCreate);
    }

    private static Client copyWithIds(Client source, Long personId, Long clientId) {
        return new Client(
                personId,
                source.getFirstName(),
                source.getLastName(),
                source.getAge(),
                source.getGenderId(),
                source.getIdentificationNumber(),
                source.getAddress(),
                source.getPostalCode(),
                source.getContactNumber(),
                source.getIdentificationTypeId(),
                source.getEmail(),
                clientId,
                source.getClientCode(),
                source.getPassword(),
                source.getStatus()
        );
    }
}
