package com.example.clinic_management.service;

import com.example.clinic_management.dto.*;
import java.util.List;

public interface AdminUserService {
    UserResponse create(AdminCreateUserRequest request);
    UserResponse update(Long userId, AdminUpdateUserRequest request);
    List<UserResponse> list();
    UserResponse getById(Long userId);
}
