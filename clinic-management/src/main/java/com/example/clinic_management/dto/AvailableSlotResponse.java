package com.example.clinic_management.dto;

import java.time.OffsetDateTime;

public class AvailableSlotResponse {
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

    public OffsetDateTime getStartTime() { return startTime; }
    public void setStartTime(OffsetDateTime startTime) { this.startTime = startTime; }

    public OffsetDateTime getEndTime() { return endTime; }
    public void setEndTime(OffsetDateTime endTime) { this.endTime = endTime; }
}
