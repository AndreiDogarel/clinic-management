package com.example.clinic_management.service;

import com.example.clinic_management.dto.LoginRequest;
import com.example.clinic_management.dto.RegisterRequest;
import com.example.clinic_management.dto.RegisterResponse;
import com.example.clinic_management.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
}
