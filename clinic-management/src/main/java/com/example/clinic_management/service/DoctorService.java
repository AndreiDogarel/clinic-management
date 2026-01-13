package com.example.clinic_management.service;

import com.example.clinic_management.dto.DoctorCreateRequest;
import com.example.clinic_management.dto.DoctorResponse;
import com.example.clinic_management.dto.DoctorUpdateRequest;
import java.util.List;

public interface DoctorService {
    DoctorResponse create(DoctorCreateRequest request);
    DoctorResponse getById(Long id);
    List<DoctorResponse> list(Long clinicId);
    DoctorResponse update(Long id, DoctorUpdateRequest request);
    void deactivate(Long id);
    void linkUser(Long doctorId, Long userId);
}
