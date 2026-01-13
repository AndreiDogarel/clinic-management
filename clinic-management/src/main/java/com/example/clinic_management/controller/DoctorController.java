package com.example.clinic_management.controller;

import com.example.clinic_management.dto.DoctorCreateRequest;
import com.example.clinic_management.dto.DoctorResponse;
import com.example.clinic_management.dto.DoctorUpdateRequest;
import com.example.clinic_management.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponse create(@Valid @RequestBody DoctorCreateRequest request) {
        return doctorService.create(request);
    }

    @GetMapping("/{id}")
    public DoctorResponse getById(@PathVariable Long id) {
        return doctorService.getById(id);
    }

    @GetMapping
    public List<DoctorResponse> list(@RequestParam(required = false) Long clinicId) {
        return doctorService.list(clinicId);
    }

    @PutMapping("/{id}")
    public DoctorResponse update(@PathVariable Long id, @Valid @RequestBody DoctorUpdateRequest request) {
        return doctorService.update(id, request);
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        doctorService.deactivate(id);
    }

    @PutMapping("/{id}/link-user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void linkUser(@PathVariable Long id, @Valid @RequestBody com.example.clinic_management.dto.DoctorLinkUserRequest request) {
        doctorService.linkUser(id, request.getUserId());
    }
}
