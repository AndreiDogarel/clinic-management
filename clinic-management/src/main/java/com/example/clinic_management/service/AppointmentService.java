package com.example.clinic_management.service;

import com.example.clinic_management.dto.AppointmentCreateRequest;
import com.example.clinic_management.dto.AppointmentResponse;
import java.time.OffsetDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentResponse create(AppointmentCreateRequest request);
    AppointmentResponse getById(Long id);
    List<AppointmentResponse> search(Long patientId, Long doctorId, OffsetDateTime from, OffsetDateTime to);
    void cancel(Long id);
    void complete(Long id);
}
