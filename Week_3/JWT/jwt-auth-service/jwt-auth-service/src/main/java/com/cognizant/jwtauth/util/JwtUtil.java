package com.cognizant.jwtauth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Builds signed JWTs.
 *
 * A JWT has three dot-separated parts: header.payload.signature.
 *   - Header:    {"alg":"HS256"} -- says which signing algorithm was used.
 *   - Payload:   the "claims" -- here, "sub" (subject/username), "iat"
 *                (issued-at timestamp), and "exp" (expiry timestamp).
 *   - Signature: HMAC-SHA256 of the header+payload, computed with a
 *                secret key only this server knows.
 * All three parts are Base64URL-encoded and joined with dots -- NOT
 * encrypted. Anyone can decode and read a JWT's header and payload
 * (try pasting one into jwt.io); what the signature guarantees is that
 * nobody can *tamper* with those claims without invalidating the
 * signature, since they don't have the secret key needed to recompute
 * it.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Generates a signed JWT for the given username.
     *
     * @param username the authenticated user's username; becomes the
     *                 token's "sub" (subject) claim
     * @return a compact, signed JWT string
     */
    public String generateToken(String username) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

}
