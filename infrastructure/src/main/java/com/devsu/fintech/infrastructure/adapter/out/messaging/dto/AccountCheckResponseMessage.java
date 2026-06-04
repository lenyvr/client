package com.devsu.fintech.infrastructure.adapter.out.messaging.dto;

public record AccountCheckResponseMessage(Long clientId, boolean hasOpenAccounts) {
}
