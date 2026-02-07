package org.apache.fineract.bff.api;

import org.apache.fineract.bff.service.DashboardDTO;
import org.apache.fineract.bff.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * REST Controller providing aggregated dashboard data for Self-Service users.
 */
@RestController
@RequestMapping("/api/v1/self")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public Mono<DashboardDTO> getDashboard() {
        return Mono.fromFuture(dashboardService.getCustomerDashboard());
    }
}