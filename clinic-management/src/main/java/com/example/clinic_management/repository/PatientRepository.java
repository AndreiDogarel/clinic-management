package com.example.clinic_management.repository;

import com.example.clinic_management.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmailIgnoreCase(String email);
    Optional<Patient> findByCnp(String cnp);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByCnp(String cnp);
}
