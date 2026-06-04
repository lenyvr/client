package com.devsu.fintech.application.usecase;

import com.devsu.fintech.domain.exception.ClientNotFoundException;
import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.domain.port.out.ClientRepositorySPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeactivateClientUseCaseImplTest {

    @Mock
    private ClientRepositorySPI clientRepository;

    private DeactivateClientUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeactivateClientUseCaseImpl(clientRepository);
    }

    @Test
    void setsStatusToFalseAndSaves() {
        Client active = ClientTestData.client(10L, 5L, "1234567", "p", true);
        when(clientRepository.findByClientId(10L)).thenReturn(Optional.of(active));
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.deactivate(10L);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        assertFalse(captor.getValue().isActive());
    }

    @Test
    void throwsWhenClientNotFound() {
        when(clientRepository.findByClientId(99L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> useCase.deactivate(99L));
        verify(clientRepository, never()).save(any());
    }
}
