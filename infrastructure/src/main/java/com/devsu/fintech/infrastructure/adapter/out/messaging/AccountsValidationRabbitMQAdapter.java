package com.devsu.fintech.infrastructure.adapter.out.messaging;

import com.devsu.fintech.domain.exception.AccountsServiceUnavailableException;
import com.devsu.fintech.domain.port.out.AccountsValidationSPI;
import com.devsu.fintech.infrastructure.adapter.out.messaging.dto.AccountCheckRequestMessage;
import com.devsu.fintech.infrastructure.adapter.out.messaging.dto.AccountCheckResponseMessage;
import com.devsu.fintech.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountsValidationRabbitMQAdapter implements AccountsValidationSPI {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public boolean hasOpenAccounts(Long clientId) {
        log.info("Checking open accounts for clientId={}", clientId);

        AccountCheckResponseMessage response = rabbitTemplate.convertSendAndReceiveAsType(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.CHECK_REQUEST_ROUTING_KEY,
                new AccountCheckRequestMessage(clientId),
                new ParameterizedTypeReference<AccountCheckResponseMessage>() {}
        );

        if (response == null) {
            log.warn("No response from accounts service for clientId={}. retrying.", clientId);
            response = (AccountCheckResponseMessage) rabbitTemplate.convertSendAndReceive(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.CHECK_REQUEST_ROUTING_KEY,
                    new AccountCheckRequestMessage(clientId)
            );
        }

        if (response == null) {
            log.warn("No response from accounts service for clientId={}. not allowing deactivation.", clientId);
            throw new AccountsServiceUnavailableException(clientId);
        }

        log.info("Accounts service response for clientId={}: hasOpenAccounts={}", clientId, response.hasOpenAccounts());
        return response.hasOpenAccounts();
    }
}
