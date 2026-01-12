package com.example.clinic_management.repository;

import com.example.clinic_management.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByMedicalRecordId(Long medicalRecordId);
}
