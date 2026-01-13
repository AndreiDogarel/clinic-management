package com.example.clinic_management.service.impl;

import com.example.clinic_management.dto.PrescriptionCreateRequest;
import com.example.clinic_management.dto.PrescriptionResponse;
import com.example.clinic_management.entity.Doctor;
import com.example.clinic_management.entity.MedicalRecord;
import com.example.clinic_management.entity.Prescription;
import com.example.clinic_management.exception.ApiException;
import com.example.clinic_management.repository.DoctorRepository;
import com.example.clinic_management.repository.MedicalRecordRepository;
import com.example.clinic_management.repository.PrescriptionRepository;
import com.example.clinic_management.service.AuditService;
import com.example.clinic_management.service.PrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final DoctorRepository doctorRepository;
    private final AuditService auditService;

    public PrescriptionServiceImpl(
            PrescriptionRepository prescriptionRepository,
            MedicalRecordRepository medicalRecordRepository,
            DoctorRepository doctorRepository,
            AuditService auditService
    ) {
        this.prescriptionRepository = prescriptionRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.doctorRepository = doctorRepository;
        this.auditService = auditService;
    }

    @Override
    public PrescriptionResponse create(Long medicalRecordId, PrescriptionCreateRequest request) {
        MedicalRecord mr = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ApiException("MEDICAL_RECORD_NOT_FOUND", "Medical record not found"));

        if (com.example.clinic_management.config.SecurityUtil.hasRole("DOCTOR")) {
            String email = com.example.clinic_management.config.SecurityUtil.currentEmail();
            if (email == null) {
                throw new ApiException("AUTH_REQUIRED", "Authentication required");
            }
            Doctor doctor = doctorRepository.findByUserAccountEmailIgnoreCase(email)
                    .orElseThrow(() -> new ApiException("DOCTOR_ACCOUNT_NOT_LINKED", "Doctor account not linked"));

            if (!mr.getDoctor().getId().equals(doctor.getId())) {
                throw new ApiException("FORBIDDEN", "Cannot add prescription for another doctor");
            }
        }

        Prescription p = new Prescription();
        p.setMedicalRecord(mr);
        p.setMedicationName(request.getMedicationName());
        p.setDosage(request.getDosage());
        p.setDurationDays(request.getDurationDays());
        p.setInstructions(request.getInstructions());

        PrescriptionResponse resp = toResponse(prescriptionRepository.save(p));

        auditService.log("PRESCRIPTION_CREATE", "Prescription", p.getId(),
                java.util.Map.of("medicalRecordId", mr.getId(), "medicationName", p.getMedicationName()));

        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> listByMedicalRecord(Long medicalRecordId) {
        if (!medicalRecordRepository.existsById(medicalRecordId)) {
            throw new ApiException("MEDICAL_RECORD_NOT_FOUND", "Medical record not found");
        }
        return prescriptionRepository.findByMedicalRecordId(medicalRecordId).stream().map(this::toResponse).toList();
    }

    private PrescriptionResponse toResponse(Prescription p) {
        PrescriptionResponse r = new PrescriptionResponse();
        r.setId(p.getId());
        r.setMedicalRecordId(p.getMedicalRecord().getId());
        r.setMedicationName(p.getMedicationName());
        r.setDosage(p.getDosage());
        r.setDurationDays(p.getDurationDays());
        r.setInstructions(p.getInstructions());
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }
}
