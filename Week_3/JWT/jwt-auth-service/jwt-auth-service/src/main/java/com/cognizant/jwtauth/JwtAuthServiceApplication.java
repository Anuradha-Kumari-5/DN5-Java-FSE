package com.cognizant.jwtauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the JWT authentication service.
 * Starts an embedded Tomcat server on port 8090 (see
 * application.properties) exposing a single endpoint, GET /authenticate,
 * that trades Basic Auth credentials for a signed JWT.
 */
@SpringBootApplication
public class JwtAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtAuthServiceApplication.class, args);
    }

}
