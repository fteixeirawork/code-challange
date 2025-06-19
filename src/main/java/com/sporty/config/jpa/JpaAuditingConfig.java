package com.sporty.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * Configuration class for JPA auditing functionality
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    /**
     * Creates an auditor provider that retrieves the current user ID for entity auditing
     *
     * @return AuditorAware implementation that provides the authenticated user's UUID
     */
    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            if (authentication.getPrincipal() instanceof UUID) {
                return Optional.of((UUID) authentication.getPrincipal());
            }

            return Optional.empty();
        };
    }
}
