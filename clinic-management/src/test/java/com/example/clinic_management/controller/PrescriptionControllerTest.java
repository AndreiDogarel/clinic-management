package com.example.clinic_management.controller;

import com.example.clinic_management.dto.PrescriptionResponse;
import com.example.clinic_management.service.PrescriptionService;
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

@WebMvcTest(PrescriptionController.class)
public class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PrescriptionService prescriptionService;

    @Test
    void create_returns201() throws Exception {
        PrescriptionResponse resp = new PrescriptionResponse();
        resp.setId(1L);
        resp.setMedicalRecordId(10L);
        resp.setMedicationName("Paracetamol");
        resp.setDosage("500mg x2/zi");
        resp.setDurationDays(5);
        resp.setInstructions("Dupa masa");
        resp.setCreatedAt(Instant.now());
        resp.setUpdatedAt(Instant.now());

        Mockito.when(prescriptionService.create(Mockito.eq(10L), Mockito.any())).thenReturn(resp);

        String body = """
        {
          "medicationName": "Paracetamol",
          "dosage": "500mg x2/zi",
          "durationDays": 5,
          "instructions": "Dupa masa"
        }
        """;

        mockMvc.perform(post("/api/medical-records/10/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.medicalRecordId").value(10))
                .andExpect(jsonPath("$.durationDays").value(5));
    }

    @Test
    void list_returns200() throws Exception {
        Mockito.when(prescriptionService.listByMedicalRecord(10L)).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/medical-records/10/prescriptions"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
