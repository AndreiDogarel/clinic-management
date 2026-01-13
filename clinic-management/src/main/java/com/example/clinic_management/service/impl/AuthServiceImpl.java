package com.example.clinic_management.service.impl;

import com.example.clinic_management.dto.*;
import com.example.clinic_management.entity.Role;
import com.example.clinic_management.entity.UserAccount;
import com.example.clinic_management.exception.ApiException;
import com.example.clinic_management.repository.UserAccountRepository;
import com.example.clinic_management.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Value("${spring.security.jwt.issuer}")
    private String issuer;

    public AuthServiceImpl(UserAccountRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ApiException("AUTH_EMAIL_EXISTS", "Email already exists");
        }

        UserAccount u = new UserAccount();
        u.setEmail(request.getEmail());
        u.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        u.setActive(true);
        u.setRoles(Set.of(Role.RECEPTIONIST));

        UserAccount saved = userRepository.save(u);

        RegisterResponse resp = new RegisterResponse();
        resp.setId(saved.getId());
        resp.setEmail(saved.getEmail());
        resp.setRoles(saved.getRoles().stream().map(Enum::name).toList());
        return resp;
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        UserAccount user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new ApiException("AUTH_INVALID", "Invalid credentials"));

        if (!user.isActive()) {
            throw new ApiException("AUTH_INACTIVE", "User inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ApiException("AUTH_INVALID", "Invalid credentials");
        }

        Instant now = Instant.now();
        Instant exp = now.plusSeconds(60 * 60);

        List<String> roles = user.getRoles().stream().map(r -> "ROLE_" + r.name()).toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(exp)
                .subject(user.getEmail())
                .claim("roles", roles)
                .build();

        JwsHeader header = JwsHeader.with(() -> "HS256").build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        TokenResponse resp = new TokenResponse();
        resp.setAccessToken(token);
        return resp;
    }
}
