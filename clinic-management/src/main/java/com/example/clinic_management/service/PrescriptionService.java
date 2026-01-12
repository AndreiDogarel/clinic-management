package com.example.clinic_management.service;

import com.example.clinic_management.dto.PrescriptionCreateRequest;
import com.example.clinic_management.dto.PrescriptionResponse;
import java.util.List;

public interface PrescriptionService {
    PrescriptionResponse create(Long medicalRecordId, PrescriptionCreateRequest request);
    List<PrescriptionResponse> listByMedicalRecord(Long medicalRecordId);
}
