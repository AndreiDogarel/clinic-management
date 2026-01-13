package com.example.clinic_management.controller;

import com.example.clinic_management.dto.*;
import com.example.clinic_management.service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody AdminCreateUserRequest request) {
        return adminUserService.create(request);
    }

    @PutMapping("/{userId}")
    public UserResponse update(@PathVariable Long userId, @RequestBody AdminUpdateUserRequest request) {
        return adminUserService.update(userId, request);
    }

    @GetMapping
    public List<UserResponse> list() {
        return adminUserService.list();
    }

    @GetMapping("/{userId}")
    public UserResponse get(@PathVariable Long userId) {
        return adminUserService.getById(userId);
    }
}
