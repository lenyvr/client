package com.devsu.fintech.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Payload to create a client. {@code clientCode} is provided by the caller and must be unique.
 */
@Schema(description = "Data required to create a client")
public record CreateClientRequest(

        @NotBlank @Schema(example = "Jose") String firstName,
        @NotBlank @Schema(example = "Lema") String lastName,
        @Positive @Schema(example = "34") int age,
        @NotNull @Schema(description = "Gender id (1=Femenino, 2=Masculino)", example = "2") Integer genderId,
        @NotBlank @Schema(example = "1234567") String identificationNumber,
        @NotNull @Schema(description = "Identification type id (1=DNI, 2=NIF, 3=PPT)", example = "1") Integer identificationTypeId,
        @NotBlank @Schema(example = "Otavalo sn y principal") String address,
        @NotBlank @Schema(example = "37890") String postalCode,
        @NotBlank @Schema(example = "098254785") String contactNumber,
        @Email @Schema(example = "jose.lema@mail.com", nullable = true) String email,
        @NotBlank @Schema(example = "C1") String clientCode,
        @NotBlank @Schema(example = "secret123") String password
) {
}
