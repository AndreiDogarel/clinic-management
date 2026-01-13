package com.example.clinic_management.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // EXCEPȚII
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/doctors/*/available-slots")
                        .hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR")

                        // admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // DOCTORS + CLINICS + SCHEDULE doar ADMIN
                        .requestMatchers("/api/clinics/**").hasRole("ADMIN")
                        .requestMatchers("/api/doctors/*/schedule/**").hasRole("ADMIN")
                        .requestMatchers("/api/doctors/**").hasRole("ADMIN")

                        // RECEPTIONIST / ADMIN
                        .requestMatchers("/api/patients/**").hasAnyRole("ADMIN", "RECEPTIONIST")
                        .requestMatchers("/api/appointments/**").hasAnyRole("ADMIN", "RECEPTIONIST")

                        // DOCTOR / ADMIN
                        .requestMatchers("/api/medical-records/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/api/medical-records/*/prescriptions/**").hasAnyRole("ADMIN", "DOCTOR")

                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .build();
    }
}
