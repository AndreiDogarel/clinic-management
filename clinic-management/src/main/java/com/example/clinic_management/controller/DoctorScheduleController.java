package com.example.clinic_management.controller;

import com.example.clinic_management.dto.*;
import com.example.clinic_management.service.DoctorScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors/{doctorId}")
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    public DoctorScheduleController(DoctorScheduleService doctorScheduleService) {
        this.doctorScheduleService = doctorScheduleService;
    }

    @PutMapping("/schedule/{dayOfWeek}")
    public DoctorScheduleResponse upsertDay(
            @PathVariable Long doctorId,
            @PathVariable short dayOfWeek,
            @Valid @RequestBody DoctorScheduleUpsertRequest request
    ) {
        return doctorScheduleService.upsertDay(doctorId, dayOfWeek, request);
    }

    @GetMapping("/schedule")
    public List<DoctorScheduleResponse> list(@PathVariable Long doctorId) {
        return doctorScheduleService.list(doctorId);
    }

    @PostMapping("/schedule/{dayOfWeek}/breaks")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorScheduleBreakResponse addBreak(
            @PathVariable Long doctorId,
            @PathVariable short dayOfWeek,
            @Valid @RequestBody DoctorScheduleBreakCreateRequest request
    ) {
        return doctorScheduleService.addBreak(doctorId, dayOfWeek, request);
    }

    @DeleteMapping("/breaks/{breakId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBreak(@PathVariable Long doctorId, @PathVariable Long breakId) {
        doctorScheduleService.deleteBreak(breakId);
    }

    @GetMapping("/available-slots")
    public List<AvailableSlotResponse> availableSlots(
            @PathVariable Long doctorId,
            @RequestParam LocalDate date,
            @RequestParam(defaultValue = "30") int durationMinutes
    ) {
        return doctorScheduleService.availableSlots(doctorId, date, durationMinutes);
    }
}
