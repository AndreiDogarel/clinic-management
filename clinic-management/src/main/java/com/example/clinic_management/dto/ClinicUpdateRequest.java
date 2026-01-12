package com.example.clinic_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClinicUpdateRequest {
    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 255)
    private String address;

    @Size(max = 40)
    private String phone;

    private Boolean active;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
