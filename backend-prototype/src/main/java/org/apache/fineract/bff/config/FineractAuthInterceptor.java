package org.apache.fineract.bff.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The "Magic" Interceptor: Automatically forwards User Credentials and Tenant ID
 * from the incoming Mobile/Web request to the outgoing Fineract API call.
 */
@Component
public class FineractAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                template.header("Authorization", authHeader);
            }

            String tenantId = request.getHeader("Fineract-Platform-TenantId");
            if (tenantId != null) {
                template.header("Fineract-Platform-TenantId", tenantId);
            }
        }
    }
}
