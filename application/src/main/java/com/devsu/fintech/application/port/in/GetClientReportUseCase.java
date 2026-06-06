package com.devsu.fintech.application.port.in;

import java.util.Optional;

public interface GetClientReportUseCase {

    Optional<ClientReportResult> getReport(String identificationNumber);
}
