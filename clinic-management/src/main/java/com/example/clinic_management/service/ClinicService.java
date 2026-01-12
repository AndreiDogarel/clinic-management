package com.example.clinic_management.service;

import com.example.clinic_management.dto.ClinicCreateRequest;
import com.example.clinic_management.dto.ClinicResponse;
import com.example.clinic_management.dto.ClinicUpdateRequest;
import java.util.List;

public interface ClinicService {
    ClinicResponse create(ClinicCreateRequest request);
    ClinicResponse getById(Long id);
    List<ClinicResponse> list();
    ClinicResponse update(Long id, ClinicUpdateRequest request);
    void deactivate(Long id);
}
