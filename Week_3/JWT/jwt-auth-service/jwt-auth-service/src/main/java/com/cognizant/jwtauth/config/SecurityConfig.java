package com.cognizant.jwtauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the authentication service.
 *
 * A subtlety worth calling out: Spring Security ships with its own
 * built-in Basic Auth handling (http.httpBasic()), which would normally
 * intercept a request carrying an "Authorization: Basic ..." header
 * before it ever reaches a controller. This exercise specifically wants
 * *us* to read and decode that header ourselves (step 2), inside
 * AuthenticationController -- not have Spring Security swallow it
 * first. So /authenticate is explicitly permitAll()'d here: that tells
 * Spring Security "let this request through untouched," which is what
 * lets the raw Authorization header reach our controller code intact.
 *
 * CSRF protection is disabled and sessions are set to STATELESS because
 * JWT-based auth doesn't use cookies/sessions -- every request is
 * expected to carry its own proof of identity (the token), so there's
 * no server-side session for CSRF to protect.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/authenticate").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

}
