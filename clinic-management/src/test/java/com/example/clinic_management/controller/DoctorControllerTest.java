package com.example.clinic_management.controller;

import com.example.clinic_management.dto.DoctorResponse;
import com.example.clinic_management.service.DoctorService;
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

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoctorService doctorService;

    @Test
    void create_returns201() throws Exception {
        DoctorResponse resp = new DoctorResponse();
        resp.setId(1L);
        resp.setClinicId(10L);
        resp.setClinicName("Clinica 1");
        resp.setFirstName("Ion");
        resp.setLastName("Ionescu");
        resp.setEmail("ion@x.com");
        resp.setSpecialization("Cardiologie");
        resp.setActive(true);
        resp.setCreatedAt(Instant.now());
        resp.setUpdatedAt(Instant.now());

        Mockito.when(doctorService.create(Mockito.any())).thenReturn(resp);

        String body = """
        {
          "clinicId": 10,
          "firstName": "Ion",
          "lastName": "Ionescu",
          "email": "ion@x.com",
          "specialization": "Cardiologie",
          "phone": "0700000001"
        }
        """;

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("ion@x.com"));
    }
}
