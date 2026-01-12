package com.example.clinic_management.controller;

import com.example.clinic_management.dto.AppointmentResponse;
import com.example.clinic_management.entity.AppointmentStatus;
import com.example.clinic_management.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;

    @Test
    void create_returns201() throws Exception {
        AppointmentResponse resp = new AppointmentResponse();
        resp.setId(1L);
        resp.setPatientId(2L);
        resp.setDoctorId(3L);
        resp.setPatientName("Ana Pop");
        resp.setDoctorName("Ion Ionescu");
        resp.setStartTime(OffsetDateTime.parse("2026-01-12T10:00:00+02:00"));
        resp.setEndTime(OffsetDateTime.parse("2026-01-12T10:30:00+02:00"));
        resp.setStatus(AppointmentStatus.SCHEDULED);

        Mockito.when(appointmentService.create(Mockito.any())).thenReturn(resp);

        String body = """
        {
          "patientId": 2,
          "doctorId": 3,
          "startTime": "2026-01-12T10:00:00+02:00",
          "endTime": "2026-01-12T10:30:00+02:00",
          "reason": "consult"
        }
        """;

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void cancel_returns204() throws Exception {
        mockMvc.perform(patch("/api/appointments/10/cancel"))
                .andExpect(status().isNoContent());
    }
}
