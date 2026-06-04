package com.devsu.fintech.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Client representation returned by the API. The password is never exposed.
 */
@Schema(description = "Client representation")
public record ClientResponse(
        Long clientId,
        Long personId,
        String firstName,
        String lastName,
        int age,
        Integer genderId,
        String identificationNumber,
        Integer identificationTypeId,
        String address,
        String postalCode,
        String contactNumber,
        String email,
        String clientCode,
        boolean status
) {
}
