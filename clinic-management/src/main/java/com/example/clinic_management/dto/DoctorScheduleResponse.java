package com.example.clinic_management.dto;

import java.time.LocalTime;
import java.util.List;

public class DoctorScheduleResponse {
    private Long id;
    private short dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean active;
    private List<DoctorScheduleBreakResponse> breaks;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public short getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(short dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public List<DoctorScheduleBreakResponse> getBreaks() { return breaks; }
    public void setBreaks(List<DoctorScheduleBreakResponse> breaks) { this.breaks = breaks; }
}
