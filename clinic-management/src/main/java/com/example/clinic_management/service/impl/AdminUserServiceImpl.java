package com.example.clinic_management.service.impl;

import com.example.clinic_management.dto.*;
import com.example.clinic_management.entity.Role;
import com.example.clinic_management.entity.UserAccount;
import com.example.clinic_management.exception.ApiException;
import com.example.clinic_management.repository.UserAccountRepository;
import com.example.clinic_management.service.AdminUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserServiceImpl(UserAccountRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse create(AdminCreateUserRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ApiException("USER_EMAIL_EXISTS", "Email already exists");
        }

        Set<Role> roles = parseRoles(request.getRoles());

        UserAccount u = new UserAccount();
        u.setEmail(request.getEmail());
        u.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        u.setActive(true);
        u.setRoles(roles);

        return toResponse(userRepository.save(u));
    }

    @Override
    public UserResponse update(Long userId, AdminUpdateUserRequest request) {
        UserAccount u = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("USER_NOT_FOUND", "User not found"));

        if (request.getRoles() != null) {
            u.setRoles(parseRoles(request.getRoles()));
        }
        if (request.getActive() != null) {
            u.setActive(request.getActive());
        }

        return toResponse(userRepository.save(u));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long userId) {
        return toResponse(userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("USER_NOT_FOUND", "User not found")));
    }

    private Set<Role> parseRoles(Set<String> raw) {
        try {
            return raw.stream().map(String::trim).map(String::toUpperCase).map(Role::valueOf).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new ApiException("ROLE_INVALID", "Invalid role value");
        }
    }

    private UserResponse toResponse(UserAccount u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setEmail(u.getEmail());
        r.setActive(u.isActive());
        r.setRoles(u.getRoles().stream().map(Enum::name).toList());
        return r;
    }
}
