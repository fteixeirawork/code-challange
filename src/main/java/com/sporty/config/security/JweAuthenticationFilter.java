package com.sporty.config.security;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Filter to authenticate requests using JWE tokens
 * <p>
 * This filter intercepts incoming HTTP requests to validate JWE tokens
 * in the Authorization header and establish user authentication.
 */
@Component
@AllArgsConstructor
public class JweAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JweUtil jweUtil;

    /**
     * Processes the request to extract and validate the JWE token
     * <p>
     * Checks for a valid Bearer token in the Authorization header,
     * validates it using JweUtil, and sets up the security context
     * with the user's identity and roles if valid.
     *
     * @param request     The HTTP request
     * @param response    The HTTP response
     * @param filterChain The filter chain for passing the request to the next filter
     * @throws IOException if an I/O exception occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        try {
            String token = extractToken(request);
            if (token != null) {
                authenticateToken(token);
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        }
    }

    /**
     * Extracts the token from the Authorization header
     *
     * @param request The HTTP request
     * @return The extracted token, or null if no valid token is present
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * Authenticates the token and sets up the security context
     *
     * @param token The token to authenticate
     */
    private void authenticateToken(String token) {
        DecodedToken decoded = jweUtil.validate(token);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        decoded.userId(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + decoded.role()))
                );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
