package com.example.clinic_management.service.impl;

import com.example.clinic_management.dto.DoctorCreateRequest;
import com.example.clinic_management.dto.DoctorResponse;
import com.example.clinic_management.dto.DoctorUpdateRequest;
import com.example.clinic_management.entity.Clinic;
import com.example.clinic_management.entity.Doctor;
import com.example.clinic_management.entity.UserAccount;
import com.example.clinic_management.exception.ApiException;
import com.example.clinic_management.repository.ClinicRepository;
import com.example.clinic_management.repository.DoctorRepository;
import com.example.clinic_management.repository.UserAccountRepository;
import com.example.clinic_management.service.AuditService;
import com.example.clinic_management.service.DoctorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final UserAccountRepository userAccountRepository;
    private final AuditService auditService;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             ClinicRepository clinicRepository,
                             UserAccountRepository userAccountRepository,
                             AuditService auditService) {
        this.doctorRepository = doctorRepository;
        this.clinicRepository = clinicRepository;
        this.userAccountRepository = userAccountRepository;
        this.auditService = auditService;
    }

    @Override
    public DoctorResponse create(DoctorCreateRequest request) {
        if (doctorRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ApiException("DOCTOR_EMAIL_EXISTS", "Email already exists");
        }
        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new ApiException("CLINIC_NOT_FOUND", "Clinic not found"));

        Doctor d = new Doctor();
        d.setClinic(clinic);
        d.setFirstName(request.getFirstName());
        d.setLastName(request.getLastName());
        d.setEmail(request.getEmail());
        d.setSpecialization(request.getSpecialization());
        d.setPhone(request.getPhone());
        d.setActive(true);

        return toResponse(doctorRepository.save(d));
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getById(Long id) {
        Doctor d = doctorRepository.findById(id).orElseThrow(() -> new ApiException("DOCTOR_NOT_FOUND", "Doctor not found"));
        return toResponse(d);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> list(Long clinicId) {
        if (clinicId == null) {
            return doctorRepository.findAll().stream().map(this::toResponse).toList();
        }
        return doctorRepository.findByClinicId(clinicId).stream().map(this::toResponse).toList();
    }

    @Override
    public DoctorResponse update(Long id, DoctorUpdateRequest request) {
        Doctor d = doctorRepository.findById(id).orElseThrow(() -> new ApiException("DOCTOR_NOT_FOUND", "Doctor not found"));

        String newEmail = request.getEmail();
        if (!d.getEmail().equalsIgnoreCase(newEmail) && doctorRepository.existsByEmailIgnoreCase(newEmail)) {
            throw new ApiException("DOCTOR_EMAIL_EXISTS", "Email already exists");
        }

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new ApiException("CLINIC_NOT_FOUND", "Clinic not found"));

        d.setClinic(clinic);
        d.setFirstName(request.getFirstName());
        d.setLastName(request.getLastName());
        d.setEmail(request.getEmail());
        d.setSpecialization(request.getSpecialization());
        d.setPhone(request.getPhone());
        if (request.getActive() != null) {
            d.setActive(request.getActive());
        }

        return toResponse(doctorRepository.save(d));
    }

    @Override
    public void deactivate(Long id) {
        Doctor d = doctorRepository.findById(id).orElseThrow(() -> new ApiException("DOCTOR_NOT_FOUND", "Doctor not found"));
        d.setActive(false);
        doctorRepository.save(d);
    }

    @Override
    public void linkUser(Long doctorId, Long userId) {
        Doctor d = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ApiException("DOCTOR_NOT_FOUND", "Doctor not found"));

        UserAccount u = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ApiException("USER_NOT_FOUND", "User not found"));

        if (!u.getRoles().contains(com.example.clinic_management.entity.Role.DOCTOR)) {
            throw new ApiException("USER_NOT_DOCTOR", "User does not have DOCTOR role");
        }

        d.setUserAccount(u);
        doctorRepository.save(d);
        auditService.log("DOCTOR_LINK_USER", "Doctor", d.getId(), java.util.Map.of("userId", u.getId()));
    }


    private DoctorResponse toResponse(Doctor d) {
        DoctorResponse r = new DoctorResponse();
        r.setId(d.getId());
        r.setClinicId(d.getClinic().getId());
        r.setClinicName(d.getClinic().getName());
        r.setFirstName(d.getFirstName());
        r.setLastName(d.getLastName());
        r.setEmail(d.getEmail());
        r.setSpecialization(d.getSpecialization());
        r.setPhone(d.getPhone());
        r.setActive(d.isActive());
        r.setCreatedAt(d.getCreatedAt());
        r.setUpdatedAt(d.getUpdatedAt());
        return r;
    }
}
