package com.example.clinic_management.repository;

import com.example.clinic_management.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<Doctor> findByEmailIgnoreCase(String email);
    List<Doctor> findByClinicId(Long clinicId);
    java.util.Optional<com.example.clinic_management.entity.Doctor> findByUserAccountEmailIgnoreCase(String email);

}
