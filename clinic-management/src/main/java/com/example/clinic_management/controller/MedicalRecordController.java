package com.example.clinic_management.controller;

import com.example.clinic_management.dto.MedicalRecordCreateRequest;
import com.example.clinic_management.dto.MedicalRecordResponse;
import com.example.clinic_management.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalRecordResponse create(@Valid @RequestBody MedicalRecordCreateRequest request) {
        return medicalRecordService.create(request);
    }

    @GetMapping("/{id}")
    public MedicalRecordResponse getById(@PathVariable Long id) {
        return medicalRecordService.getById(id);
    }

    @GetMapping
    public List<MedicalRecordResponse> list(@RequestParam(required = false) Long patientId) {
        return medicalRecordService.list(patientId);
    }
}
