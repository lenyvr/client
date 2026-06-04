package com.devsu.fintech.domain.port.out;

public interface AccountsValidationSPI {

    boolean hasOpenAccounts(Long clientId);
}
