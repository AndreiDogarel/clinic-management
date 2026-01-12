package com.example.clinic_management.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

public class DoctorScheduleBreakCreateRequest {

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @Size(max = 120)
    private String note;

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
