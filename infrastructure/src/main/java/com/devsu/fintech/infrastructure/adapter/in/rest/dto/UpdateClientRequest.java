package com.devsu.fintech.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Payload to update a client. {@code password} is optional: when omitted the current one is kept.
 */
@Schema(description = "Data to update a client")
public record UpdateClientRequest(

        @NotBlank String firstName,
        @NotBlank String lastName,
        @Positive int age,
        @NotNull @Schema(description = "Gender id (1=Femenino, 2=Masculino)") Integer genderId,
        @NotBlank String identificationNumber,
        @NotNull @Schema(description = "Identification type id (1=DNI, 2=NIF, 3=PPT)") Integer identificationTypeId,
        @NotBlank String address,
        @NotBlank String postalCode,
        @NotBlank String contactNumber,
        @Email @Schema(nullable = true) String email,
        @NotBlank String clientCode,
        @Schema(description = "New password; leave empty to keep the current one", nullable = true) String password
) {
}
