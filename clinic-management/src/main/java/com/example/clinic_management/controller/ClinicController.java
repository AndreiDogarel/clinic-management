package com.example.clinic_management.controller;

import com.example.clinic_management.dto.ClinicCreateRequest;
import com.example.clinic_management.dto.ClinicResponse;
import com.example.clinic_management.dto.ClinicUpdateRequest;
import com.example.clinic_management.service.ClinicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clinics")
public class ClinicController {

    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClinicResponse create(@Valid @RequestBody ClinicCreateRequest request) {
        return clinicService.create(request);
    }

    @GetMapping("/{id}")
    public ClinicResponse getById(@PathVariable Long id) {
        return clinicService.getById(id);
    }

    @GetMapping
    public List<ClinicResponse> list() {
        return clinicService.list();
    }

    @PutMapping("/{id}")
    public ClinicResponse update(@PathVariable Long id, @Valid @RequestBody ClinicUpdateRequest request) {
        return clinicService.update(id, request);
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        clinicService.deactivate(id);
    }
}
