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

);
private void configureSessions(HttpSecurity http) throws Exception
private static final String JWT_SECRET =
NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey)
private static final String JWT_ISSUER = "http://localhost:3000";
private void configureAuthorization(HttpSecurity http) throws Exception
private void configureCsrf(HttpSecurity http) throws Exception
private static final String JWT_AUDIENCE = "cartel-control";
auth.requestMatchers("/", "/index.html", "/cartel/**", "/error", "/favicon.ico").permitAll();
return http.build();
.build();
configureCsrf(http);
http.csrf(csrf -> csrf.disable());
OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer(JWT_ISSUER);
JwtDecoder jwtDecoder()
http.authorizeHttpRequests(auth ->
OAuth2TokenValidator<Jwt> audienceValidator = new JwtAudienceValidator(JWT_AUDIENCE);
http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
http.oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));
auth.anyRequest().permitAll();
.macAlgorithm(MacAlgorithm.HS256)
auth.requestMatchers("/api/cartel/**").authenticated();
configureJwt(http);
"marcus-the-worm-has-a-secret-plot-to-get-jimbo-james-out-of-jail";
return decoder;
configureSessions(http);
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
private void configureJwt(HttpSecurity http) throws Exception
decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(issuerValidator, audienceValidator));
SecretKey secretKey = new SecretKeySpec(JWT_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
configureAuthorization(http);
}
