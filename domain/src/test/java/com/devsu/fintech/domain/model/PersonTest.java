package com.devsu.fintech.domain.model;

import com.devsu.fintech.domain.exception.InvalidPersonDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonTest {

    private Person newPerson(String firstName, int age, Integer genderId, String email) {
        return new Person(1L, firstName, "Lema", age, genderId, "1234567",
                "Otavalo sn", "37890", "098254785", 1, email);
    }

    @Test
    void buildsValidPerson() {
        Person person = newPerson("Jose", 34, 2, "jose@mail.com");

        assertEquals("Jose", person.getFirstName());
        assertEquals(34, person.getAge());
        assertEquals(2, person.getGenderId());
    }

    @Test
    void emailIsOptional() {
        assertDoesNotThrow(() -> newPerson("Jose", 34, 2, null));
        assertNull(newPerson("Jose", 34, 2, null).getEmail());
    }

    @Test
    void rejectsBlankFirstName() {
        assertThrows(InvalidPersonDataException.class, () -> newPerson("  ", 34, 2, null));
    }

    @Test
    void rejectsNonPositiveAge() {
        assertThrows(InvalidPersonDataException.class, () -> newPerson("Jose", 0, 2, null));
    }

    @Test
    void rejectsNullGender() {
        assertThrows(InvalidPersonDataException.class, () -> newPerson("Jose", 34, null, null));
    }
}
