package com.example.clinic_management.repository;

import com.example.clinic_management.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientId(Long patientId);
    boolean existsByAppointmentId(Long appointmentId);
}
