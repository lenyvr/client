package com.devsu.fintech.infrastructure.adapter.in.messaging;

import com.devsu.fintech.application.port.in.GetClientReportUseCase;
import com.devsu.fintech.infrastructure.adapter.in.messaging.dto.ClientReportRequestDTO;
import com.devsu.fintech.infrastructure.adapter.in.messaging.dto.ClientReportResponseDTO;
import com.devsu.fintech.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientReportRabbitMQConsumer {

    private final GetClientReportUseCase getClientReportUseCase;

    @RabbitListener(queues = RabbitMQConfig.CLIENT_REPORT_REQUEST_QUEUE)
    public ClientReportResponseDTO handleClientReportRequest(ClientReportRequestDTO request) {
        log.info("Received client report request for identificationNumber={}", request.identificationNumber());

        return getClientReportUseCase.getReport(request.identificationNumber())
                .map(result -> new ClientReportResponseDTO(
                        result.clientId(),
                        result.firstName(),
                        result.lastName(),
                        result.identificationNumber(),
                        result.identificationType(),
                        result.address(),
                        result.email(),
                        result.contactNumber()
                ))
                .orElseGet(() -> {
                    log.warn("No active client found for identificationNumber={}", request.identificationNumber());
                    return null;
                });
    }
}
