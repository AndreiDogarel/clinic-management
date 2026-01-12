package com.example.clinic_management.dto;

import java.time.LocalTime;

public class DoctorScheduleBreakResponse {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String note;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
