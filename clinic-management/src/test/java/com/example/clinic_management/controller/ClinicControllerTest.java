package com.example.clinic_management.controller;

import com.example.clinic_management.dto.ClinicResponse;
import com.example.clinic_management.service.ClinicService;
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

@WebMvcTest(ClinicController.class)
public class ClinicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClinicService clinicService;

    @Test
    void create_returns201() throws Exception {
        ClinicResponse resp = new ClinicResponse();
        resp.setId(1L);
        resp.setName("Clinica 1");
        resp.setActive(true);
        resp.setCreatedAt(Instant.now());
        resp.setUpdatedAt(Instant.now());

        Mockito.when(clinicService.create(Mockito.any())).thenReturn(resp);

        String body = """
        {
          "name": "Clinica 1",
          "address": "Bucuresti",
          "phone": "0700000000"
        }
        """;

        mockMvc.perform(post("/api/clinics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Clinica 1"));
    }
}
