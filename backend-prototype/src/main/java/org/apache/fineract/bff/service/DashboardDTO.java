package org.apache.fineract.bff.service;

import org.apache.fineract.bff.core.ClientRecord;
import org.apache.fineract.bff.core.AccountSummaryRecord;

public record DashboardDTO(
        ClientRecord profile,
        AccountSummaryRecord accounts
) {}