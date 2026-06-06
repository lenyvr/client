package com.devsu.fintech.infrastructure.adapter.out.persistence;

import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.infrastructure.adapter.out.persistence.entity.ClientEntity;
import com.devsu.fintech.infrastructure.adapter.out.persistence.entity.PersonEntity;
import lombok.RequiredArgsConstructor;

/**
 * Maps between the {@link Client} domain entity and its JPA representation
 * ({@link ClientEntity} + {@link PersonEntity}).
 */
@RequiredArgsConstructor
final class ClientPersistenceMapper {

    static ClientEntity toEntity(Client client) {
        PersonEntity person = new PersonEntity();
        person.setPersonId(client.getPersonId());
        person.setFirstName(client.getFirstName());
        person.setLastName(client.getLastName());
        person.setAge(client.getAge());
        person.setGenderId(client.getGenderId());
        person.setIdentificationNumber(client.getIdentificationNumber());
        person.setAddress(client.getAddress());
        person.setPostalCode(client.getPostalCode());
        person.setContactNumber(client.getContactNumber());
        person.setIdentificationTypeId(client.getIdentificationTypeId());
        person.setEmail(client.getEmail());

        ClientEntity entity = new ClientEntity();
        entity.setClientId(client.getClientId());
        entity.setPerson(person);
        entity.setClientCode(client.getClientCode());
        entity.setPassword(client.getPassword());
        entity.setStatus(client.getStatus());
        return entity;
    }

    static Client toDomain(ClientEntity entity) {
        PersonEntity person = entity.getPerson();
        return new Client(
                person.getPersonId(),
                person.getFirstName(),
                person.getLastName(),
                person.getAge(),
                person.getGenderId(),
                person.getIdentificationNumber(),
                person.getAddress(),
                person.getPostalCode(),
                person.getContactNumber(),
                person.getIdentificationTypeId(),
                person.getEmail(),
                entity.getClientId(),
                entity.getClientCode(),
                entity.getPassword(),
                entity.isStatus()
        );
    }
}
