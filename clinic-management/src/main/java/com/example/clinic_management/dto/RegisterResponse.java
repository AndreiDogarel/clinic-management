package com.example.clinic_management.dto;

import java.util.List;

public class RegisterResponse {
    private Long id;
    private String email;
    private List<String> roles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
