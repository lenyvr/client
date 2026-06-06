package com.devsu.fintech.application.port.in;

public record ClientReportResult(
        Long clientId,
        String firstName,
        String lastName,
        String identificationNumber,
        String identificationType,
        String address,
        String email,
        String contactNumber
) {
}
