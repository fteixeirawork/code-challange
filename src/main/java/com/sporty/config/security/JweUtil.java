package com.sporty.config.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.DirectDecrypter;
import net.minidev.json.JSONObject;

import java.time.Instant;
import java.util.UUID;

/**
 * Utility class for JWE (JSON Web Encryption) token operations
 */
public class JweUtil {

    // 256‑bit (32‑byte) shared secret (keep safe!)
    private static final byte[] SECRET = "0123456789abcdef0123456789abcdef".getBytes();

    /**
     * Generates an encrypted JWE token for a user
     *
     * @param userId The unique identifier of the user
     * @param role The role of the user
     * @return String containing the serialized JWE token
     * @throws RuntimeException if token creation fails
     */
    public static String generateToken(UUID userId, String role) {
        try {
            JSONObject claims = new JSONObject();
            long now = Instant.now().getEpochSecond();
            long exp = now + 3600;

            claims.put("sub", userId.toString());
            claims.put("role", role);
            claims.put("iat", Instant.now().getEpochSecond());
            claims.put("exp", exp);

            // Create unsecured payload (simpler than full JWT signing)
            Payload payload = new Payload(claims);

            JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM);
            JWEObject jweObject = new JWEObject(header, payload);

            jweObject.encrypt(new DirectEncrypter(SECRET));

            return jweObject.serialize();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create token", ex);
        }
    }

    /**
     * Validates and decodes a JWE token
     *
     * @param token The JWE token string to validate
     * @return DecodedToken containing the user ID and role from the token
     * @throws RuntimeException if token is invalid or expired
     */
    public static DecodedToken validate(String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);
            jweObject.decrypt(new DirectDecrypter(SECRET));
            JSONObject json = new JSONObject(jweObject.getPayload().toJSONObject());

            long now = Instant.now().getEpochSecond();
            long exp = ((Number) json.get("exp")).longValue();
            if (now > exp) {
                throw new RuntimeException("Token expired");
            }

            UUID userId = UUID.fromString((String) json.get("sub"));
            String role = (String) json.get("role");

            return new DecodedToken(userId, role);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid token", ex);
        }
    }
}
