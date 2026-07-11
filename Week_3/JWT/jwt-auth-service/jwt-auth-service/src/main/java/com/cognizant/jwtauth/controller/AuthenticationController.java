package com.cognizant.jwtauth.controller;

import com.cognizant.jwtauth.dto.TokenResponse;
import com.cognizant.jwtauth.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * Exposes GET /authenticate: exchanges Basic Auth credentials for a JWT.
 *
 * This is intentionally NOT relying on Spring Security's built-in
 * httpBasic() authentication filter. Instead -- per this exercise's
 * step 2 -- it reads the raw "Authorization" header itself and decodes
 * it by hand, so the mechanics of Basic Auth are explicit rather than
 * hidden behind a framework feature.
 */
@RestController
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private static final String BASIC_PREFIX = "Basic ";

    // A hardcoded credential store, just for this exercise -- in a real
    // service this would be a UserDetailsService backed by a database,
    // with passwords stored hashed (e.g. BCrypt), never in plain text.
    private static final Map<String, String> USER_STORE = Map.of("user", "pwd");

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {

        logger.info("START - authenticate()");

        if (authHeader == null || !authHeader.startsWith(BASIC_PREFIX)) {
            logger.warn("Request missing a valid Basic Authorization header");
            return unauthorized("Missing or malformed Authorization header. "
                    + "Expected: Authorization: Basic <base64(username:password)>");
        }

        // Step 2: read + decode the header.
        // "Basic dXNlcjpwd2Q=" -> strip "Basic " -> Base64-decode the
        // remainder -> "user:pwd" -> split into username and password.
        String base64Credentials = authHeader.substring(BASIC_PREFIX.length()).trim();

        String decodedCredentials;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            decodedCredentials = new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            logger.warn("Authorization header was not valid Base64");
            return unauthorized("Authorization header is not valid Base64");
        }

        // Split on the FIRST colon only, in case a password legitimately
        // contains a colon character.
        String[] parts = decodedCredentials.split(":", 2);
        if (parts.length != 2) {
            logger.warn("Decoded credentials were not in username:password form");
            return unauthorized("Malformed credentials");
        }

        String username = parts[0];
        String password = parts[1];

        // Validate against the (hardcoded, for this exercise) user store.
        if (!USER_STORE.containsKey(username) || !USER_STORE.get(username).equals(password)) {
            logger.warn("Authentication failed for username: {}", username);
            return unauthorized("Invalid username or password");
        }

        // Step 3: generate the token for the user we just retrieved.
        String token = jwtUtil.generateToken(username);

        logger.info("END - authenticate()");
        return ResponseEntity.ok(new TokenResponse(token));
    }

    private ResponseEntity<?> unauthorized(String message) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"jwt-auth-service\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("error", message));
    }

}
