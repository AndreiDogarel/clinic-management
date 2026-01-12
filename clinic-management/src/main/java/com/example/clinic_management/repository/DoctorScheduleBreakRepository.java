package com.example.clinic_management.repository;

import com.example.clinic_management.entity.DoctorScheduleBreak;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorScheduleBreakRepository extends JpaRepository<DoctorScheduleBreak, Long> {
    List<DoctorScheduleBreak> findByScheduleIdOrderByStartTimeAsc(Long scheduleId);
}
