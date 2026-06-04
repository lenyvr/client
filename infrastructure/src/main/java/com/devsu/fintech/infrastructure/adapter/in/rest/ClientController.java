package com.devsu.fintech.infrastructure.adapter.in.rest;

import com.devsu.fintech.application.port.in.CreateClientUseCase;
import com.devsu.fintech.application.port.in.DeactivateClientUseCase;
import com.devsu.fintech.application.port.in.ListClientsUseCase;
import com.devsu.fintech.application.port.in.UpdateClientUseCase;
import com.devsu.fintech.infrastructure.adapter.in.rest.dto.ClientResponse;
import com.devsu.fintech.infrastructure.adapter.in.rest.dto.CreateClientRequest;
import com.devsu.fintech.infrastructure.adapter.in.rest.dto.UpdateClientRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Client management operations")
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final ListClientsUseCase listClientsUseCase;
    private final DeactivateClientUseCase deactivateClientUseCase;

    public ClientController(CreateClientUseCase createClientUseCase,
                            UpdateClientUseCase updateClientUseCase,
                            ListClientsUseCase listClientsUseCase,
                            DeactivateClientUseCase deactivateClientUseCase) {
        this.createClientUseCase = createClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.listClientsUseCase = listClientsUseCase;
        this.deactivateClientUseCase = deactivateClientUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a client",
            description = "Creates a client. If an inactive client exists for the same identification "
                    + "number it is updated and reactivated; if it is active a 409 is returned.")
    public ClientResponse create(@Valid @RequestBody CreateClientRequest request) {
        log.info("Creating client with identification {}", request.identificationNumber());
        return ClientRestMapper.toResponse(createClientUseCase.create(ClientRestMapper.toDomain(request)));
    }

    @PutMapping("/{clientId}")
    @Operation(summary = "Update a client", description = "Updates a client and its person data.")
    public ClientResponse update(@PathVariable Long clientId,
                                 @Valid @RequestBody UpdateClientRequest request) {
        log.info("Updating client {}", clientId);
        return ClientRestMapper.toResponse(
                updateClientUseCase.update(clientId, ClientRestMapper.toCommand(request)));
    }

    @GetMapping
    @Operation(summary = "List active clients",
            description = "Lists active clients. Optional case-insensitive filter matches name, "
                    + "identification number or client code.")
    public List<ClientResponse> list(@RequestParam(required = false) String filter) {
        log.info("Listing active clients with filter '{}'", filter);
        return listClientsUseCase.list(filter).stream()
                .map(ClientRestMapper::toResponse)
                .toList();
    }

    @DeleteMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deactivate a client", description = "Soft delete: sets the client status to false.")
    public void deactivate(@PathVariable Long clientId) {
        log.info("Deactivating client {}", clientId);
        deactivateClientUseCase.deactivate(clientId);
    }
}
