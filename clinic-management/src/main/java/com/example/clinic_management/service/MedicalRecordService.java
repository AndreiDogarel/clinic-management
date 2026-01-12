package com.example.clinic_management.service;

import com.example.clinic_management.dto.MedicalRecordCreateRequest;
import com.example.clinic_management.dto.MedicalRecordResponse;
import java.util.List;

public interface MedicalRecordService {
    MedicalRecordResponse create(MedicalRecordCreateRequest request);
    MedicalRecordResponse getById(Long id);
    List<MedicalRecordResponse> list(Long patientId);
}
