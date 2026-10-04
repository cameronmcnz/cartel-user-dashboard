package com.mcnz.cartel;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtAudienceValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;



public class SecurityConfig {

    private static final String JWT_SECRET =
            "marcus-the-worm-has-a-secret-plot-to-get-jimbo-james-out-of-jail";

    private static final String JWT_ISSUER = "http://localhost:3000";
    private static final String JWT_AUDIENCE = "cartel-control";

    
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        configureCsrf(http);
        configureSessions(http);
        configureAuthorization(http);
        configureJwt(http);

        return http.build();
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
    }

    private void configureSessions(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    private void configureAuthorization(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/", "/index.html", "/cartel/**", "/error", "/favicon.ico").permitAll();
            auth.requestMatchers("/api/cartel/**").authenticated();
            auth.anyRequest().permitAll();
        });
    }

    private void configureJwt(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));
    }

    
    JwtDecoder jwtDecoder() {

        SecretKey secretKey = new SecretKeySpec(JWT_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer(JWT_ISSUER);
        OAuth2TokenValidator<Jwt> audienceValidator = new JwtAudienceValidator(JWT_AUDIENCE);

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(issuerValidator, audienceValidator));

        return decoder;
    }
}
