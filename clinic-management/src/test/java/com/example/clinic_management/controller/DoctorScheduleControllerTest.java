package com.example.clinic_management.controller;

import com.example.clinic_management.service.DoctorScheduleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorScheduleController.class)
public class DoctorScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoctorScheduleService doctorScheduleService;

    @Test
    void upsertDay_returns200() throws Exception {
        Mockito.when(doctorScheduleService.upsertDay(Mockito.eq(10L), Mockito.eq((short)1), Mockito.any()))
                .thenReturn(new com.example.clinic_management.dto.DoctorScheduleResponse());

        String body = """
        { "startTime": "09:00:00", "endTime": "17:00:00", "active": true }
        """;

        mockMvc.perform(put("/api/doctors/10/schedule/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void availableSlots_returns200() throws Exception {
        Mockito.when(doctorScheduleService.availableSlots(Mockito.eq(10L), Mockito.any(), Mockito.anyInt()))
                .thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/doctors/10/available-slots?date=2026-01-13&durationMinutes=30"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
