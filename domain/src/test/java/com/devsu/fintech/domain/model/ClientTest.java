package com.devsu.fintech.domain.model;

import com.devsu.fintech.domain.exception.InvalidClientDataException;
import com.devsu.fintech.domain.exception.InvalidPersonDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientTest {

    private Client newClient(String clientCode, String password, boolean status) {
        return new Client(1L, "Jose", "Lema", 34, 2, "1234567",
                "Otavalo sn", "37890", "098254785", 1, "jose@mail.com",
                10L, clientCode, password, status);
    }

    @Test
    void buildsValidClientAndInheritsPersonData() {
        Client client = newClient("C1", "secret", true);

        assertEquals("C1", client.getClientCode());
        assertEquals("Jose", client.getFirstName()); // inherited from Person
        assertTrue(client.isActive());
    }

    @Test
    void activateAndDeactivateToggleStatus() {
        Client client = newClient("C1", "secret", false);
        assertFalse(client.isActive());

        client.activate();
        assertTrue(client.isActive());

        client.deactivate();
        assertFalse(client.isActive());
    }

    @Test
    void rejectsBlankClientCode() {
        assertThrows(InvalidClientDataException.class, () -> newClient("  ", "secret", true));
    }

    @Test
    void rejectsBlankPassword() {
        assertThrows(InvalidClientDataException.class, () -> newClient("C1", "", true));
    }

    @Test
    void stillEnforcesPersonInvariants() {
        assertThrows(InvalidPersonDataException.class, () -> new Client(1L, "", "Lema", 34, 2,
                "1234567", "Otavalo", "37890", "098", 1, null, 10L, "C1", "secret", true));
    }
}
