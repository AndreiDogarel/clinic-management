package com.example.clinic_management.repository;

import com.example.clinic_management.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, short dayOfWeek);
    List<DoctorSchedule> findByDoctorIdOrderByDayOfWeekAsc(Long doctorId);
}
