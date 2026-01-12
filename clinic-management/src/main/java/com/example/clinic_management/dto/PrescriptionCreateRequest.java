package com.example.clinic_management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PrescriptionCreateRequest {

    @NotBlank
    @Size(max = 150)
    private String medicationName;

    @NotBlank
    @Size(max = 120)
    private String dosage;

    @Min(1)
    private int durationDays;

    @Size(max = 500)
    private String instructions;

    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}
