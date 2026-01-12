package com.example.clinic_management.service.impl;

import com.example.clinic_management.dto.AppointmentCreateRequest;
import com.example.clinic_management.dto.AppointmentResponse;
import com.example.clinic_management.entity.*;
import com.example.clinic_management.exception.ApiException;
import com.example.clinic_management.repository.AppointmentRepository;
import com.example.clinic_management.repository.DoctorRepository;
import com.example.clinic_management.repository.PatientRepository;
import com.example.clinic_management.service.AppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public AppointmentResponse create(AppointmentCreateRequest request) {
        if (request.getStartTime().compareTo(request.getEndTime()) >= 0) {
            throw new ApiException("APPOINTMENT_INVALID_INTERVAL", "startTime must be before endTime");
        }

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ApiException("PATIENT_NOT_FOUND", "Patient not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ApiException("DOCTOR_NOT_FOUND", "Doctor not found"));

        boolean overlap = appointmentRepository.existsDoctorOverlap(
                doctor.getId(),
                request.getStartTime(),
                request.getEndTime(),
                AppointmentStatus.CANCELLED
        );

        if (overlap) {
            throw new ApiException("APPOINTMENT_DOCTOR_OVERLAP", "Doctor already has an appointment in that interval");
        }

        if (!patient.isActive()) {
            throw new ApiException("PATIENT_INACTIVE", "Patient is inactive");
        }
        if (!doctor.isActive()) {
            throw new ApiException("DOCTOR_INACTIVE", "Doctor is inactive");
        }
        if (!doctor.getClinic().isActive()) {
            throw new ApiException("CLINIC_INACTIVE", "Clinic is inactive");
        }


        Appointment a = new Appointment();
        a.setPatient(patient);
        a.setDoctor(doctor);
        a.setStartTime(request.getStartTime());
        a.setEndTime(request.getEndTime());
        a.setReason(request.getReason());
        a.setStatus(AppointmentStatus.SCHEDULED);

        return toResponse(appointmentRepository.save(a));
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getById(Long id) {
        Appointment a = appointmentRepository.findById(id)
                .orElseThrow(() -> new ApiException("APPOINTMENT_NOT_FOUND", "Appointment not found"));
        return toResponse(a);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> search(Long patientId, Long doctorId, OffsetDateTime from, OffsetDateTime to) {
        return appointmentRepository.search(patientId, doctorId, from, to).stream().map(this::toResponse).toList();
    }

    @Override
    public void cancel(Long id) {
        Appointment a = appointmentRepository.findById(id)
                .orElseThrow(() -> new ApiException("APPOINTMENT_NOT_FOUND", "Appointment not found"));

        if (a.getStatus() == AppointmentStatus.CANCELLED) {
            return;
        }
        if (a.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ApiException("APPOINTMENT_INVALID_STATUS", "Cannot cancel a completed appointment");
        }

        a.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(a);
    }

    @Override
    public void complete(Long id) {
        Appointment a = appointmentRepository.findById(id)
                .orElseThrow(() -> new ApiException("APPOINTMENT_NOT_FOUND", "Appointment not found"));

        if (a.getStatus() == AppointmentStatus.COMPLETED) {
            return;
        }
        if (a.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ApiException("APPOINTMENT_INVALID_STATUS", "Cannot complete a cancelled appointment");
        }

        a.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(a);
    }

    private AppointmentResponse toResponse(Appointment a) {
        AppointmentResponse r = new AppointmentResponse();
        r.setId(a.getId());
        r.setPatientId(a.getPatient().getId());
        r.setPatientName(a.getPatient().getFirstName() + " " + a.getPatient().getLastName());
        r.setDoctorId(a.getDoctor().getId());
        r.setDoctorName(a.getDoctor().getFirstName() + " " + a.getDoctor().getLastName());
        r.setStartTime(a.getStartTime());
        r.setEndTime(a.getEndTime());
        r.setStatus(a.getStatus());
        r.setReason(a.getReason());
        r.setCreatedAt(a.getCreatedAt());
        r.setUpdatedAt(a.getUpdatedAt());
        return r;
    }
}
