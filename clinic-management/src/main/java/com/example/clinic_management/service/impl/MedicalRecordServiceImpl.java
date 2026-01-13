package com.example.clinic_management.service.impl;

import com.example.clinic_management.dto.MedicalRecordCreateRequest;
import com.example.clinic_management.dto.MedicalRecordResponse;
import com.example.clinic_management.entity.*;
import com.example.clinic_management.exception.ApiException;
import com.example.clinic_management.repository.AppointmentRepository;
import com.example.clinic_management.repository.DoctorRepository;
import com.example.clinic_management.repository.MedicalRecordRepository;
import com.example.clinic_management.repository.PatientRepository;
import com.example.clinic_management.service.AuditService;
import com.example.clinic_management.service.MedicalRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final AuditService auditService;

    public MedicalRecordServiceImpl(
            MedicalRecordRepository medicalRecordRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            AuditService auditService
    ) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.auditService = auditService;
    }

    @Override
    public MedicalRecordResponse create(MedicalRecordCreateRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ApiException("PATIENT_NOT_FOUND", "Patient not found"));
        if (!patient.isActive()) {
            throw new ApiException("PATIENT_INACTIVE", "Patient is inactive");
        }

        Doctor doctor;
        if (com.example.clinic_management.config.SecurityUtil.hasRole("DOCTOR")) {
            String email = com.example.clinic_management.config.SecurityUtil.currentEmail();
            if (email == null) {
                throw new ApiException("AUTH_REQUIRED", "Authentication required");
            }
            doctor = doctorRepository.findByUserAccountEmailIgnoreCase(email)
                    .orElseThrow(() -> new ApiException("DOCTOR_ACCOUNT_NOT_LINKED", "Doctor account not linked"));
        } else {
            doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new ApiException("DOCTOR_NOT_FOUND", "Doctor not found"));
        }

        if (!doctor.isActive()) {
            throw new ApiException("DOCTOR_INACTIVE", "Doctor is inactive");
        }
        if (!doctor.getClinic().isActive()) {
            throw new ApiException("CLINIC_INACTIVE", "Clinic is inactive");
        }

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ApiException("APPOINTMENT_NOT_FOUND", "Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new ApiException("APPOINTMENT_NOT_COMPLETED", "Appointment must be completed");
        }

        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new ApiException("APPOINTMENT_PATIENT_MISMATCH", "Appointment does not belong to patient");
        }

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new ApiException("APPOINTMENT_DOCTOR_MISMATCH", "Appointment does not belong to doctor");
        }

        if (medicalRecordRepository.existsByAppointmentId(appointment.getId())) {
            throw new ApiException("MEDICAL_RECORD_EXISTS", "Medical record already exists for this appointment");
        }

        MedicalRecord mr = new MedicalRecord();
        mr.setPatient(patient);
        mr.setDoctor(doctor);
        mr.setAppointment(appointment);
        mr.setDiagnosis(request.getDiagnosis());
        mr.setNotes(request.getNotes());

        MedicalRecordResponse resp = toResponse(medicalRecordRepository.save(mr));

        auditService.log("MEDICAL_RECORD_CREATE", "MedicalRecord", mr.getId(),
                java.util.Map.of("appointmentId", appointment.getId(), "patientId", patient.getId(), "doctorId", doctor.getId()));

        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordResponse getById(Long id) {
        MedicalRecord mr = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ApiException("MEDICAL_RECORD_NOT_FOUND", "Medical record not found"));
        return toResponse(mr);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> list(Long patientId) {
        if (patientId == null) {
            return medicalRecordRepository.findAll().stream().map(this::toResponse).toList();
        }
        return medicalRecordRepository.findByPatientId(patientId).stream().map(this::toResponse).toList();
    }

    private MedicalRecordResponse toResponse(MedicalRecord mr) {
        MedicalRecordResponse r = new MedicalRecordResponse();
        r.setId(mr.getId());
        r.setPatientId(mr.getPatient().getId());
        r.setPatientName(mr.getPatient().getFirstName() + " " + mr.getPatient().getLastName());
        r.setDoctorId(mr.getDoctor().getId());
        r.setDoctorName(mr.getDoctor().getFirstName() + " " + mr.getDoctor().getLastName());
        r.setAppointmentId(mr.getAppointment().getId());
        r.setDiagnosis(mr.getDiagnosis());
        r.setNotes(mr.getNotes());
        r.setCreatedAt(mr.getCreatedAt());
        r.setUpdatedAt(mr.getUpdatedAt());
        return r;
    }
}
