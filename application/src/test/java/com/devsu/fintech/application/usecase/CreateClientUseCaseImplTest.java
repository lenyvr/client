package com.devsu.fintech.application.usecase;

import com.devsu.fintech.domain.exception.ClientAlreadyExistsException;
import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.domain.port.out.ClientRepositorySPI;
import com.devsu.fintech.domain.port.out.PasswordEncoderSPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateClientUseCaseImplTest {

    @Mock
    private ClientRepositorySPI clientRepository;
    @Mock
    private PasswordEncoderSPI passwordEncoder;

    private CreateClientUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateClientUseCaseImpl(clientRepository, passwordEncoder);
    }

    @Test
    void createsNewClientEncodingPasswordAndForcingActiveStatus() {
        Client request = ClientTestData.newRequest("1234567", "raw-pass");
        when(clientRepository.findByIdentificationNumber("1234567")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("raw-pass")).thenReturn("encoded-pass");
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        Client result = useCase.create(request);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        Client saved = captor.getValue();
        assertEquals("encoded-pass", saved.getPassword());
        assertTrue(saved.isActive());
        assertTrue(result.isActive());
    }

    @Test
    void throwsWhenActiveClientAlreadyExists() {
        Client request = ClientTestData.newRequest("1234567", "raw-pass");
        Client existingActive = ClientTestData.client(10L, 5L, "1234567", "old", true);
        when(clientRepository.findByIdentificationNumber("1234567")).thenReturn(Optional.of(existingActive));

        assertThrows(ClientAlreadyExistsException.class, () -> useCase.create(request));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void reactivatesInactiveClientKeepingExistingIds() {
        Client request = ClientTestData.newRequest("1234567", "new-pass");
        Client existingInactive = ClientTestData.client(10L, 5L, "1234567", "old", false);
        when(clientRepository.findByIdentificationNumber("1234567")).thenReturn(Optional.of(existingInactive));
        when(passwordEncoder.encode("new-pass")).thenReturn("encoded-new");
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.create(request);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        Client saved = captor.getValue();
        assertEquals(10L, saved.getClientId());
        assertEquals(5L, saved.getPersonId());
        assertEquals("encoded-new", saved.getPassword());
        assertTrue(saved.isActive());
    }
}
