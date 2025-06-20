package com.sporty.adapter.in.web;

import com.sporty.adapter.in.web.schema.LoginSchema;
import com.sporty.adapter.in.web.schema.TokenSchema;
import com.sporty.config.security.JweUtil;
import com.sporty.domain.User;
import com.sporty.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * REST controller for authentication operations
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private static final Map<String, User> USER_STORE = new ConcurrentHashMap<>();
    private final JweUtil jweUtil;

    static {
        USER_STORE.put("user", User.builder().userId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).username("user").password("user").role(UserRole.USER).build());
        USER_STORE.put("agent", User.builder().userId(UUID.fromString("456e4567-e89b-12d3-a456-426614174000")).username("agent").password("agent").role(UserRole.AGENT).build());
    }

    /**
     * Authenticates a user with provided credentials and issues a JWE token
     *
     * @param req Login request containing username and password
     * @return ResponseEntity with token if authentication succeeds, or 401 if it fails
     */
    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TokenSchema> login(@RequestBody LoginSchema req) {
        User user = USER_STORE.get(req.username());

        if (user == null || !user.getPassword().equals(req.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jweUtil.generateToken(user.getUserId(), user.getRole().toString());

        return ResponseEntity.ok(new TokenSchema(token));
    }
}
