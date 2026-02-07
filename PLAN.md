#FINERACT-2439 Self-Service API (BFF) POC Implementation Plan

#Summary
This plan outlines the creation of a standalone Backend-for-Frontend (BFF) service for Apache Fineract. It aims to provide a simplified, secure, and high-performance API layer for consumer-facing applications (Mobile/Web), replacing the deprecated built-in self-service APIs with a modern architectural pattern.
#Goals

    Provide a single entry point for consumer applications to fetch aggregated data.
    Implement Principal Pass-through security (User Credential forwarding).
    Improve mobile performance via parallel backend requests and data thinning.
    Ensure strict tenant isolation using Fineract’s header-based multi-tenancy.
    Deliver a production-ready "Dashboard" endpoint as the core Proof of Concept (POC).

#Non-Goals

    Do not modify the core Fineract (fineract-provider) codebase.
    Do not create a new database for banking transactions (BFF is stateless or uses a minimal cache/session DB only).
    Do not implement administrative or staff-level functionality.
    Do not build the end-user mobile UI (outside of scope for this backend POC).

#Decisions and Constraints

    Repository: Standalone repository (or new module in a Fineract fork).
    Build Tool: Gradle (consistent with Fineract 1.x/2.x).
    Language: Java 17+ with Spring Boot 3.x.
    Communication: Spring Cloud OpenFeign for REST communication with Fineract.
    Security: Spring Security with Basic/JWT pass-through logic.

#MVP Scope

    User Discovery: Integration with /userdetails to resolve clientId upon login.
    Aggregated Dashboard: A single /self/dashboard endpoint combining:
        Client Profile (/clients/{id})
        Account Summaries (/clients/{id}/accounts)
        Recent Transactions (derived from account details)
    Parallel Processing: Use of CompletableFuture to fetch data concurrently.
    Data Masking: Logic to obscure sensitive PII (e.g., masking account numbers).
    OpenAPI Documentation: Integrated Swagger UI for API testing.

#High-Level Architecture

    Security Filter: Extracts Authorization and TenantId from the incoming request.
    Controller Layer: Receives requests and validates basic parameters.
    Aggregator Service: Orchestrates parallel calls to the Fineract Gateway.
    Fineract Gateway (Feign): Maps types and paths to the Fineract 1.x/2.x API.
    Auth Interceptor: Automatically injects the user's credentials into every outgoing Fineract call.
    Normalization Layer: Flattens complex Fineract JSON responses into a lean DashboardDTO.

#Security Strategy (Principal Pass-through)

    No Credential Storage: The BFF acts as a "Secure Proxy." It never stores user passwords.
    Permission Mirroring: By forwarding the user's own credentials, the BFF respects Fineract's internal RBAC/Permissions.
    Header Injection: Every request to Fineract includes:
        Authorization: User's Base64/JWT token.
        Fineract-Platform-TenantId: The specific tenant identifier.

#Data Model (DashboardDTO)
A flattened structure optimized for mobile network conditions:

    profile: Name, image (base64/link), and status.
    accounts: List of objects containing accountNo (masked), balance, and currency.
    loans: List of active loans with nextInstallmentDate and amountDue.
    meta: Server timestamp (UTC) and status flags.

#Implementation Detail (Java Files)

    FineractClient.java: Feign interface defining Fineract REST paths.
    FineractAuthInterceptor.java: Captures and forwards security headers.
    DashboardService.java: Logic for async execution and data merging.
    DashboardController.java: The REST entry point for the consumer app.
    GlobalExceptionHandler.java: Maps Feign/Fineract errors to user-friendly messages.

#Testing Strategy

    Unit Testing: Mock Fineract responses using JUnit 5 and Mockito.
    Concurrency Test: Verify that the dashboard response time is the duration of the slowest single call, not the sum of all calls.
    Security Test: Ensure a request without a TenantId or Authorization header is rejected by the BFF with a 401 Unauthorized.

#CI and Deployment

    Docker: Multi-stage Dockerfile to build and run the Spring Boot app.
    Compose: docker-compose.yml providing a one-click setup for the BFF + a mock Fineract container for local testing.

#Open Questions

    Should the BFF implement a local H2 database for short-term caching of the clientId mapping?
    Which Fineract version (1.9.x or 2.0) should be the primary target for the POC environment?
