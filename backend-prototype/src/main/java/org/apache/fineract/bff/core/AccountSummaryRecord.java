package org.apache.fineract.bff.core;

import java.util.List;

public record AccountSummaryRecord(List<SavingsSummaryRecord> savingsAccounts, List<LoanSummaryRecord> loanAccounts) {}

record SavingsSummaryRecord(Long id, String accountNo, Double accountBalance, String currencyCode) {}

record LoanSummaryRecord(Long id, String accountNo, Double loanBalance, String currencyCode) {}