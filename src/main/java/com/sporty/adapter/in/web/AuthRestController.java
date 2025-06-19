package com.sporty.adapter.in.web;

import com.sporty.adapter.in.web.schema.LoginSchema;
import com.sporty.adapter.in.web.schema.TokenSchema;
import com.sporty.config.security.JweUtil;
import com.sporty.domain.User;
import com.sporty.domain.UserRole;
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
public class AuthRestController {

    private static final Map<String, User> USER_STORE = new ConcurrentHashMap<>();

    static {
        USER_STORE.put("user", new User(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "user", "user", UserRole.USER));
        USER_STORE.put("agent", new User(UUID.fromString("456e4567-e89b-12d3-a456-426614174000"), "agent", "agent", UserRole.AGENT));
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

        String token = JweUtil.generateToken(user.getUserId(), user.getRole().toString());

        return ResponseEntity.ok(new TokenSchema(token));
    }
}
