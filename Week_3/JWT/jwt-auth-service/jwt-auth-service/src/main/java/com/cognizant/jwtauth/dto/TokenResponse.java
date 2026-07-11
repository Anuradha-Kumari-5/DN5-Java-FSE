package com.cognizant.jwtauth.dto;

/**
 * Response shape for a successful /authenticate call.
 *
 * A Java record is used here because it's a pure, immutable data
 * carrier -- exactly what a response DTO should be. Jackson (Spring
 * Boot's default JSON library) serializes the "token" component
 * directly into a "token" JSON field, giving us:
 *   { "token": "..." }
 */
public record TokenResponse(String token) {
}
