package com.example.clinic_management.controller;

import com.example.clinic_management.dto.PatientResponse;
import com.example.clinic_management.service.PatientService;
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

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @Test
    void create_returns201() throws Exception {
        PatientResponse resp = new PatientResponse();
        resp.setId(1L);
        resp.setFirstName("Ana");
        resp.setLastName("Pop");
        resp.setEmail("ana@x.com");
        resp.setActive(true);
        resp.setCreatedAt(Instant.now());
        resp.setUpdatedAt(Instant.now());

        Mockito.when(patientService.create(Mockito.any())).thenReturn(resp);

        String body = """
        {
          "firstName": "Ana",
          "lastName": "Pop",
          "email": "ana@x.com",
          "phone": "0700000000",
          "birthDate": "1990-01-01",
          "cnp": "123"
        }
        """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("ana@x.com"));
    }

    @Test
    void list_returns200() throws Exception {
        Mockito.when(patientService.list()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void deactivate_returns204() throws Exception {
        mockMvc.perform(patch("/api/patients/10/deactivate"))
                .andExpect(status().isNoContent());
    }
}
