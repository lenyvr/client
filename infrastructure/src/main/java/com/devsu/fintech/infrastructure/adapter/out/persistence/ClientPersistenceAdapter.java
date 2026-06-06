package com.devsu.fintech.infrastructure.adapter.out.persistence;

import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.domain.port.out.ClientRepositorySPI;
import com.devsu.fintech.infrastructure.adapter.out.persistence.entity.ClientEntity;
import com.devsu.fintech.infrastructure.adapter.out.persistence.repository.ClientJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Output adapter implementing {@link ClientRepositorySPI} over Spring Data JPA.
 * Saving a client cascades its person, so the person is persisted first.
 */
@Repository
public class ClientPersistenceAdapter implements ClientRepositorySPI {

    private final ClientJpaRepository clientJpaRepository;

    public ClientPersistenceAdapter(ClientJpaRepository clientJpaRepository) {
        this.clientJpaRepository = clientJpaRepository;
    }

    @Override
    public Optional<Client> findByIdentificationNumber(String identificationNumber) {
        return clientJpaRepository.findByPerson_IdentificationNumber(identificationNumber)
                .map(ClientPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Client> findByClientId(Long clientId) {
        return clientJpaRepository.findByStatusAndClientId (clientId, ClientStatus.ACTIVE)
                .map(ClientPersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public Client save(Client client) {
        ClientEntity saved = clientJpaRepository.save(ClientPersistenceMapper.toEntity(client));
        return ClientPersistenceMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> searchActive(String filter) {
        return clientJpaRepository.searchActive(filter).stream()
                .map(ClientPersistenceMapper::toDomain)
                .toList();
    }
}
