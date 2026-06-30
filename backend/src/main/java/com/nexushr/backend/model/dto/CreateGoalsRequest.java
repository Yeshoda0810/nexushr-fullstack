package com.nexushr.backend.model.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateGoalsRequest {
    @NotNull
    private Integer reviewYear;

    @NotBlank
    private String reviewPeriod; // "Q1", "Q2", "Q3", "Q4", "Annual"

    @NotBlank
    private String goals;

    private String achievements;
}
