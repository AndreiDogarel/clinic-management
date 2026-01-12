package com.example.clinic_management.service.impl;

import com.example.clinic_management.dto.PatientCreateRequest;
import com.example.clinic_management.dto.PatientResponse;
import com.example.clinic_management.dto.PatientUpdateRequest;
import com.example.clinic_management.entity.Patient;
import com.example.clinic_management.exception.ApiException;
import com.example.clinic_management.repository.PatientRepository;
import com.example.clinic_management.service.PatientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientResponse create(PatientCreateRequest request) {
        if (patientRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ApiException("PATIENT_EMAIL_EXISTS", "Email already exists");
        }
        if (request.getCnp() != null && !request.getCnp().isBlank() && patientRepository.existsByCnp(request.getCnp())) {
            throw new ApiException("PATIENT_CNP_EXISTS", "CNP already exists");
        }
        Patient p = new Patient();
        applyCreate(p, request);
        p.setActive(true);
        return toResponse(patientRepository.save(p));
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getById(Long id) {
        return toResponse(patientRepository.findById(id).orElseThrow(() -> new ApiException("PATIENT_NOT_FOUND", "Patient not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponse> list() {
        return patientRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public PatientResponse update(Long id, PatientUpdateRequest request) {
        Patient p = patientRepository.findById(id).orElseThrow(() -> new ApiException("PATIENT_NOT_FOUND", "Patient not found"));

        String newEmail = request.getEmail();
        if (!p.getEmail().equalsIgnoreCase(newEmail) && patientRepository.existsByEmailIgnoreCase(newEmail)) {
            throw new ApiException("PATIENT_EMAIL_EXISTS", "Email already exists");
        }

        String newCnp = request.getCnp();
        if (newCnp != null && !newCnp.isBlank()) {
            boolean changed = p.getCnp() == null || !p.getCnp().equals(newCnp);
            if (changed && patientRepository.existsByCnp(newCnp)) {
                throw new ApiException("PATIENT_CNP_EXISTS", "CNP already exists");
            }
        }

        p.setFirstName(request.getFirstName());
        p.setLastName(request.getLastName());
        p.setEmail(request.getEmail());
        p.setPhone(request.getPhone());
        p.setBirthDate(request.getBirthDate());
        p.setCnp(request.getCnp());
        if (request.getActive() != null) {
            p.setActive(request.getActive());
        }

        return toResponse(patientRepository.save(p));
    }

    @Override
    public void deactivate(Long id) {
        Patient p = patientRepository.findById(id).orElseThrow(() -> new ApiException("PATIENT_NOT_FOUND", "Patient not found"));
        p.setActive(false);
        patientRepository.save(p);
    }

    private void applyCreate(Patient p, PatientCreateRequest request) {
        p.setFirstName(request.getFirstName());
        p.setLastName(request.getLastName());
        p.setEmail(request.getEmail());
        p.setPhone(request.getPhone());
        p.setBirthDate(request.getBirthDate());
        p.setCnp(request.getCnp());
    }

    private PatientResponse toResponse(Patient p) {
        PatientResponse r = new PatientResponse();
        r.setId(p.getId());
        r.setFirstName(p.getFirstName());
        r.setLastName(p.getLastName());
        r.setEmail(p.getEmail());
        r.setPhone(p.getPhone());
        r.setBirthDate(p.getBirthDate());
        r.setCnp(p.getCnp());
        r.setActive(p.isActive());
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }
}
