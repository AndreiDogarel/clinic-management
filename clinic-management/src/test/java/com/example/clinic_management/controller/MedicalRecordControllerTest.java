package com.example.clinic_management.controller;

import com.example.clinic_management.dto.MedicalRecordResponse;
import com.example.clinic_management.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    @Test
    void create_returns201() throws Exception {
        MedicalRecordResponse resp = new MedicalRecordResponse();
        resp.setId(1L);
        resp.setPatientId(2L);
        resp.setDoctorId(3L);
        resp.setAppointmentId(4L);
        resp.setDiagnosis("Dx");
        resp.setNotes("Notes");
        resp.setCreatedAt(Instant.now());
        resp.setUpdatedAt(Instant.now());

        Mockito.when(medicalRecordService.create(Mockito.any())).thenReturn(resp);

        String body = """
        {
          "patientId": 2,
          "doctorId": 3,
          "appointmentId": 4,
          "diagnosis": "Dx",
          "notes": "Notes"
        }
        """;

        mockMvc.perform(post("/api/medical-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.diagnosis").value("Dx"));
    }

    @Test
    void list_returns200() throws Exception {
        Mockito.when(medicalRecordService.list(Mockito.any())).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/medical-records?patientId=2"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
