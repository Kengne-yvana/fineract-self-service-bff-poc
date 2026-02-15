package org.apache.fineract.bff.core;

public record UserRecord(Long userId, String username, Long clientId, String officeName) {}
