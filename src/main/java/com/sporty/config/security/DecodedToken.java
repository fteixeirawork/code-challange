package com.sporty.config.security;

import java.util.UUID;

public record DecodedToken(
        UUID userId,
        String role
) {
}
