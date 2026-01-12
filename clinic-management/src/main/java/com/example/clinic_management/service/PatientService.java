package com.example.clinic_management.service;

import com.example.clinic_management.dto.PatientCreateRequest;
import com.example.clinic_management.dto.PatientResponse;
import com.example.clinic_management.dto.PatientUpdateRequest;
import java.util.List;

public interface PatientService {
    PatientResponse create(PatientCreateRequest request);
    PatientResponse getById(Long id);
    List<PatientResponse> list();
    PatientResponse update(Long id, PatientUpdateRequest request);
    void deactivate(Long id);
}
