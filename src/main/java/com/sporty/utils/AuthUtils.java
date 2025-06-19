package com.sporty.utils;

import com.sporty.domain.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * Utility class for authentication-related operations
 */
public class AuthUtils {

    /**
     * Extract user role from Authentication object
     *
     * @param auth Authentication object
     * @return UserRole of the authenticated user
     * @throws IllegalStateException if no role is found
     */
    public static UserRole getUserRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .map(UserRole::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User role not found in authentication"));
    }

    /**
     * Get user role from current security context
     *
     * @return UserRole of the currently authenticated user
     */
    public static UserRole getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return getUserRole(auth);
    }

    /**
     * Get principal ID from current security context
     *
     * @return UUID of the currently authenticated user
     */
    public static UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) auth.getPrincipal();
    }
}
