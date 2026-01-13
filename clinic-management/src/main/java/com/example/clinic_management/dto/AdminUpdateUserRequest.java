package com.example.clinic_management.dto;

import java.util.Set;

public class AdminUpdateUserRequest {
    private Set<String> roles;
    private Boolean active;

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
