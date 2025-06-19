package com.sporty.config.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
public class JweAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Processes the request to extract and validate the JWE token
     * <p>
     * Checks for a valid Bearer token in the Authorization header,
     * validates it using JweUtil, and sets up the security context
     * with the user's identity and roles if valid.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param filterChain The filter chain for passing the request to the next filter
     * @throws ServletException if a servlet exception occurs
     * @throws IOException if an I/O exception occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                DecodedToken decoded = JweUtil.validate(token);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                decoded.userId(),
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + decoded.role()))
                        );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
