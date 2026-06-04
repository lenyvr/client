package com.devsu.fintech.application.usecase;

import com.devsu.fintech.domain.model.Client;
import com.devsu.fintech.domain.port.out.ClientRepositorySPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListClientsUseCaseImplTest {

    @Mock
    private ClientRepositorySPI clientRepository;

    private ListClientsUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListClientsUseCaseImpl(clientRepository);
    }

    @Test
    void delegatesFilterToRepository() {
        Client client = ClientTestData.client(10L, 5L, "1234567", "p", true);
        when(clientRepository.searchActive("jose")).thenReturn(List.of(client));

        List<Client> result = useCase.list("jose");

        assertEquals(1, result.size());
        verify(clientRepository).searchActive("jose");
    }

    @Test
    void passesNullFilterThrough() {
        when(clientRepository.searchActive(null)).thenReturn(List.of());

        useCase.list(null);

        verify(clientRepository).searchActive(null);
    }
}
