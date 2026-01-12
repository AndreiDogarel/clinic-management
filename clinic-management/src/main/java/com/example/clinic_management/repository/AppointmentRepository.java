package com.example.clinic_management.repository;

import com.example.clinic_management.entity.Appointment;
import com.example.clinic_management.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        select (count(a) > 0)
        from Appointment a
        where a.doctor.id = :doctorId
          and a.status <> :cancelled
          and (a.startTime < :endTime and a.endTime > :startTime)
    """)
    boolean existsDoctorOverlap(
            @Param("doctorId") Long doctorId,
            @Param("startTime") OffsetDateTime startTime,
            @Param("endTime") OffsetDateTime endTime,
            @Param("cancelled") AppointmentStatus cancelled
    );

    @Query("""
        select a
        from Appointment a
        where (:patientId is null or a.patient.id = :patientId)
          and (:doctorId is null or a.doctor.id = :doctorId)
          and (:from is null or a.startTime >= :from)
          and (:to is null or a.startTime <= :to)
        order by a.startTime asc
    """)
    List<Appointment> search(
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId,
            @Param("from") OffsetDateTime from,
            @Param("to") OffsetDateTime to
    );

    List<com.example.clinic_management.entity.Appointment> findByDoctorIdAndStartTimeBetween(Long doctorId, java.time.OffsetDateTime from, java.time.OffsetDateTime to);
}
