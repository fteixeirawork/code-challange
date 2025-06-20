package com.sporty.config.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.sporty.config.security.exception.JweTokenException;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

/**
 * Utility class for JWE (JSON Web Encryption) token operations
 * <p>
 * Provides functionality for generating, validating and decoding JWE tokens
 * used for authentication and authorization.
 */
@Component
public class JweUtil {

    private static final JWEAlgorithm ALGORITHM = JWEAlgorithm.DIR;
    private static final EncryptionMethod ENCRYPTION_METHOD = EncryptionMethod.A256GCM;
    private static final long TOKEN_VALIDITY_SECONDS = 3600;
    private static final int REQUIRED_KEY_LENGTH = 32;

    private final byte[] secret;

    /**
     * Initializes the JWE utility with the configured secret
     *
     * @param secretKey The base64-encoded secret key used for encryption
     */
    public JweUtil(@Value("${security.jwe.secret:0123456789abcdef0123456789abcdef}") String secretKey) {
        this.secret = secretKey.getBytes();

        if (this.secret.length < REQUIRED_KEY_LENGTH) {
            throw new IllegalArgumentException(
                    "Secret key must be at least " + REQUIRED_KEY_LENGTH + " bytes long for " + ENCRYPTION_METHOD.getName());
        }
    }

    /**
     * Generates an encrypted JWE token for a user
     *
     * @param userId The unique identifier of the user
     * @param role   The role of the user
     * @return String containing the serialized JWE token
     * @throws JweTokenException if token creation fails
     */
    public String generateToken(UUID userId, String role) {
        try {
            JSONObject claims = new JSONObject();
            Instant now = Instant.now();
            Instant expiration = now.plus(TOKEN_VALIDITY_SECONDS, ChronoUnit.SECONDS);

            claims.put("sub", userId.toString());
            claims.put("role", role);
            claims.put("iat", now.getEpochSecond());
            claims.put("exp", expiration.getEpochSecond());

            JWEHeader header = new JWEHeader(ALGORITHM, ENCRYPTION_METHOD);
            JWEObject jweObject = new JWEObject(header, new Payload(claims));

            jweObject.encrypt(new DirectEncrypter(secret));

            return jweObject.serialize();
        } catch (JOSEException ex) {
            throw new JweTokenException("Failed to create token", ex);
        }
    }

    /**
     * Validates and decodes a JWE token
     *
     * @param token The JWE token string to validate
     * @return DecodedToken containing the user ID and role from the token
     * @throws JweTokenException if token is invalid, expired, or malformed
     */
    public DecodedToken validate(String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);
            jweObject.decrypt(new DirectDecrypter(secret));
            Map<String, Object> json = jweObject.getPayload().toJSONObject();

            long now = Instant.now().getEpochSecond();
            long exp = ((Number) json.get("exp")).longValue();
            if (now > exp) {
                throw new JweTokenException("Token expired");
            }

            UUID userId = UUID.fromString((String) json.get("sub"));
            String role = (String) json.get("role");

            return new DecodedToken(userId, role);
        } catch (ParseException ex) {
            throw new JweTokenException("Malformed token", ex);
        } catch (JOSEException ex) {
            throw new JweTokenException("Failed to decrypt token", ex);
        } catch (ClassCastException | IllegalArgumentException ex) {
            throw new JweTokenException("Invalid token payload", ex);
        } catch (Exception ex) {
            throw new JweTokenException("Invalid token", ex);
        }
    }
}
