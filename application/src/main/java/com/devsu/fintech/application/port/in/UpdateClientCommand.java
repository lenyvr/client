package com.devsu.fintech.application.port.in;

/**
 * Editable client data for an update. {@code password} is optional: when null or blank the
 * current (already-encoded) password is kept. {@code status} is not part of the update
 * (see {@link DeactivateClientUseCase}).
 */
public record UpdateClientCommand(
        String firstName,
        String lastName,
        int age,
        Integer genderId,
        String identificationNumber,
        String address,
        String postalCode,
        String contactNumber,
        Integer identificationTypeId,
        String email,
        String clientCode,
        String password
) {
}
