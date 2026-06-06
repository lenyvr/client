package com.devsu.fintech.infrastructure.adapter.in.messaging;

import com.devsu.fintech.application.port.in.VerifyClientUseCase;
import com.devsu.fintech.infrastructure.adapter.in.messaging.dto.VerifyClientRequestDTO;
import com.devsu.fintech.infrastructure.adapter.in.messaging.dto.VerifyClientResponseDTO;
import com.devsu.fintech.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerifyClientRabbitMQConsumer {

    private final VerifyClientUseCase verifyClientUseCase;

    @RabbitListener(queues = RabbitMQConfig.CLIENT_VERIFY_REQUEST_QUEUE)
    public VerifyClientResponseDTO handleVerifyClientRequest(VerifyClientRequestDTO request) {
        log.info("Received verify client request for clientId={}", request.clientId());
        boolean exists = verifyClientUseCase.existsActiveClient(request.clientId());
        log.info("Verify client result for clientId={}: exists={}", request.clientId(), exists);
        return new VerifyClientResponseDTO(exists);
    }
}
