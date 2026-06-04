package com.devsu.fintech.infrastructure.adapter.in.rest;

import com.devsu.fintech.application.port.in.UpdateClientCommand;
import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.infrastructure.adapter.in.rest.dto.ClientResponse;
import com.devsu.fintech.infrastructure.adapter.in.rest.dto.CreateClientRequest;
import com.devsu.fintech.infrastructure.adapter.in.rest.dto.UpdateClientRequest;

/**
 * Maps between REST DTOs and application/domain types.
 */
final class ClientRestMapper {

    private ClientRestMapper() {
    }

    static Client toDomain(CreateClientRequest request) {
        return new Client(
                null,
                request.firstName(),
                request.lastName(),
                request.age(),
                request.genderId(),
                request.identificationNumber(),
                request.address(),
                request.postalCode(),
                request.contactNumber(),
                request.identificationTypeId(),
                request.email(),
                null,
                request.clientCode(),
                request.password(),
                true
        );
    }

    static UpdateClientCommand toCommand(UpdateClientRequest request) {
        return new UpdateClientCommand(
                request.firstName(),
                request.lastName(),
                request.age(),
                request.genderId(),
                request.identificationNumber(),
                request.address(),
                request.postalCode(),
                request.contactNumber(),
                request.identificationTypeId(),
                request.email(),
                request.clientCode(),
                request.password()
        );
    }

    static ClientResponse toResponse(Client client) {
        return new ClientResponse(
                client.getClientId(),
                client.getPersonId(),
                client.getFirstName(),
                client.getLastName(),
                client.getAge(),
                client.getGenderId(),
                client.getIdentificationNumber(),
                client.getIdentificationTypeId(),
                client.getAddress(),
                client.getPostalCode(),
                client.getContactNumber(),
                client.getEmail(),
                client.getClientCode(),
                client.getStatus()
        );
    }
}
