package com.example.clinic_management.dto;

import jakarta.validation.constraints.NotNull;

public class DoctorLinkUserRequest {
    @NotNull
    private Long userId;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
