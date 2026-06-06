package com.devsu.fintech.application.usecase;

import com.devsu.fintech.application.port.in.ClientReportResult;
import com.devsu.fintech.application.port.in.GetClientReportUseCase;
import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.domain.port.out.ClientRepositorySPI;
import com.devsu.fintech.domain.port.out.IdentificationTypeLookupSPI;

import java.util.Optional;

public class GetClientReportUseCaseImpl implements GetClientReportUseCase {

    private final ClientRepositorySPI clientRepository;
    private final IdentificationTypeLookupSPI identificationTypeLookup;

    public GetClientReportUseCaseImpl(ClientRepositorySPI clientRepository,
                                      IdentificationTypeLookupSPI identificationTypeLookup) {
        this.clientRepository = clientRepository;
        this.identificationTypeLookup = identificationTypeLookup;
    }

    @Override
    public Optional<ClientReportResult> getReport(String identificationNumber) {
        return clientRepository.findByIdentificationNumber(identificationNumber)
                .filter(Client::isActive)
                .map(client -> new ClientReportResult(
                        client.getClientId(),
                        client.getFirstName(),
                        client.getLastName(),
                        client.getIdentificationNumber(),
                        identificationTypeLookup.findNameById(client.getIdentificationTypeId()),
                        client.getAddress(),
                        client.getEmail(),
                        client.getContactNumber()
                ));
    }
}
