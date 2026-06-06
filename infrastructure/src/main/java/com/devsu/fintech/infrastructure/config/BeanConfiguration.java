package com.devsu.fintech.infrastructure.config;

import com.devsu.fintech.application.port.in.CreateClientUseCase;
import com.devsu.fintech.application.port.in.DeactivateClientUseCase;
import com.devsu.fintech.application.port.in.GetClientReportUseCase;
import com.devsu.fintech.application.port.in.ListClientsUseCase;
import com.devsu.fintech.application.port.in.UpdateClientUseCase;
import com.devsu.fintech.application.port.in.VerifyClientUseCase;
import com.devsu.fintech.application.usecase.CreateClientUseCaseImpl;
import com.devsu.fintech.application.usecase.DeactivateClientUseCaseImpl;
import com.devsu.fintech.application.usecase.GetClientReportUseCaseImpl;
import com.devsu.fintech.application.usecase.ListClientsUseCaseImpl;
import com.devsu.fintech.application.usecase.UpdateClientUseCaseImpl;
import com.devsu.fintech.application.usecase.VerifyClientUseCaseImpl;
import com.devsu.fintech.domain.port.out.AccountsValidationSPI;
import com.devsu.fintech.domain.port.out.ClientRepositorySPI;
import com.devsu.fintech.domain.port.out.IdentificationTypeLookupSPI;
import com.devsu.fintech.domain.port.out.PasswordEncoderSPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Manual wiring of the (Spring-free) application use cases as beans, injecting the
 * infrastructure port implementations.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public CreateClientUseCase createClientUseCase(ClientRepositorySPI clientRepository,
                                                   PasswordEncoderSPI passwordEncoder) {
        return new CreateClientUseCaseImpl(clientRepository, passwordEncoder);
    }

    @Bean
    public UpdateClientUseCase updateClientUseCase(ClientRepositorySPI clientRepository,
                                                   PasswordEncoderSPI passwordEncoder) {
        return new UpdateClientUseCaseImpl(clientRepository, passwordEncoder);
    }

    @Bean
    public ListClientsUseCase listClientsUseCase(ClientRepositorySPI clientRepository) {
        return new ListClientsUseCaseImpl(clientRepository);
    }

    @Bean
    public DeactivateClientUseCase deactivateClientUseCase(ClientRepositorySPI clientRepository,
                                                           AccountsValidationSPI accountsValidation) {
        return new DeactivateClientUseCaseImpl(clientRepository, accountsValidation);
    }

    @Bean
    public GetClientReportUseCase getClientReportUseCase(ClientRepositorySPI clientRepository,
                                                         IdentificationTypeLookupSPI identificationTypeLookup) {
        return new GetClientReportUseCaseImpl(clientRepository, identificationTypeLookup);
    }

    @Bean
    public VerifyClientUseCase verifyClientUseCase(ClientRepositorySPI clientRepository) {
        return new VerifyClientUseCaseImpl(clientRepository);
    }
}
