package com.example.clinic_management.service.impl;

import com.example.clinic_management.dto.ClinicCreateRequest;
import com.example.clinic_management.dto.ClinicResponse;
import com.example.clinic_management.dto.ClinicUpdateRequest;
import com.example.clinic_management.entity.Clinic;
import com.example.clinic_management.exception.ApiException;
import com.example.clinic_management.repository.ClinicRepository;
import com.example.clinic_management.service.ClinicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;

    public ClinicServiceImpl(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    @Override
    public ClinicResponse create(ClinicCreateRequest request) {
        Clinic c = new Clinic();
        c.setName(request.getName());
        c.setAddress(request.getAddress());
        c.setPhone(request.getPhone());
        c.setActive(true);
        return toResponse(clinicRepository.save(c));
    }

    @Override
    @Transactional(readOnly = true)
    public ClinicResponse getById(Long id) {
        return toResponse(clinicRepository.findById(id).orElseThrow(() -> new ApiException("CLINIC_NOT_FOUND", "Clinic not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClinicResponse> list() {
        return clinicRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ClinicResponse update(Long id, ClinicUpdateRequest request) {
        Clinic c = clinicRepository.findById(id).orElseThrow(() -> new ApiException("CLINIC_NOT_FOUND", "Clinic not found"));
        c.setName(request.getName());
        c.setAddress(request.getAddress());
        c.setPhone(request.getPhone());
        if (request.getActive() != null) {
            c.setActive(request.getActive());
        }
        return toResponse(clinicRepository.save(c));
    }

    @Override
    public void deactivate(Long id) {
        Clinic c = clinicRepository.findById(id).orElseThrow(() -> new ApiException("CLINIC_NOT_FOUND", "Clinic not found"));
        c.setActive(false);
        clinicRepository.save(c);
    }

    private ClinicResponse toResponse(Clinic c) {
        ClinicResponse r = new ClinicResponse();
        r.setId(c.getId());
        r.setName(c.getName());
        r.setAddress(c.getAddress());
        r.setPhone(c.getPhone());
        r.setActive(c.isActive());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedAt(c.getUpdatedAt());
        return r;
    }
}
