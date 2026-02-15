package org.apache.fineract.bff.core;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign Client for connecting to the Apache Fineract REST API.
 * Uses JavaRecords for immutable data mapping.
 */
@FeignClient(name = "fineract-client", url = "${fineract.api.url}")
public interface FineractClient {

    @GetMapping("/userdetails")
    UserRecord getUserDetails();

    @GetMapping("/clients/{clientId}")
    ClientRecord getClient(@PathVariable("clientId") Long clientId);

    @GetMapping("/clients/{clientId}/accounts")
    AccountSummaryRecord getAccounts(@PathVariable("clientId") Long clientId);
}