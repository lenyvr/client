package com.devsu.fintech.application.usecase;

import com.devsu.fintech.application.port.in.UpdateClientCommand;
import com.devsu.fintech.domain.exception.ClientNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateClientUseCaseImplTest {

    @Mock
    private ClientRepositorySPI clientRepository;
    @Mock
    private PasswordEncoderSPI passwordEncoder;

    private UpdateClientUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateClientUseCaseImpl(clientRepository, passwordEncoder);
    }

    private UpdateClientCommand command(String password) {
        return new UpdateClientCommand("Maria", "Diaz", 40, 1, "1234567",
                "New address", "11111", "099999999", 2, "maria@mail.com", "C1", password);
    }

    @Test
    void throwsWhenClientNotFound() {
        when(clientRepository.findByClientId(99L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> useCase.update(99L, command("x")));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void reEncodesPasswordWhenProvided() {
        Client current = ClientTestData.client(10L, 5L, "1234567", "old-encoded", true);
        when(clientRepository.findByClientId(10L)).thenReturn(Optional.of(current));
        when(passwordEncoder.encode("new-pass")).thenReturn("new-encoded");
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.update(10L, command("new-pass"));

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        assertEquals("new-encoded", captor.getValue().getPassword());
        assertEquals("Maria", captor.getValue().getFirstName());
    }

    @Test
    void keepsCurrentPasswordWhenBlank() {
        Client current = ClientTestData.client(10L, 5L, "1234567", "old-encoded", true);
        when(clientRepository.findByClientId(10L)).thenReturn(Optional.of(current));
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.update(10L, command("   "));

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        assertEquals("old-encoded", captor.getValue().getPassword());
        verify(passwordEncoder, never()).encode(any());
    }
}
