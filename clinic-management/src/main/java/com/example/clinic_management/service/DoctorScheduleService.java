package com.example.clinic_management.service;

import com.example.clinic_management.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleService {
    DoctorScheduleResponse upsertDay(Long doctorId, short dayOfWeek, DoctorScheduleUpsertRequest request);
    List<DoctorScheduleResponse> list(Long doctorId);
    DoctorScheduleBreakResponse addBreak(Long doctorId, short dayOfWeek, DoctorScheduleBreakCreateRequest request);
    void deleteBreak(Long breakId);
    List<AvailableSlotResponse> availableSlots(Long doctorId, LocalDate date, int durationMinutes);
}
