package com.example.clinic_management.controller;

import com.example.clinic_management.dto.PrescriptionCreateRequest;
import com.example.clinic_management.dto.PrescriptionResponse;
import com.example.clinic_management.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records/{recordId}/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrescriptionResponse create(
            @PathVariable Long recordId,
            @Valid @RequestBody PrescriptionCreateRequest request
    ) {
        return prescriptionService.create(recordId, request);
    }

    @GetMapping
    public List<PrescriptionResponse> list(@PathVariable Long recordId) {
        return prescriptionService.listByMedicalRecord(recordId);
    }
}
