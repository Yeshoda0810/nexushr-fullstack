package com.nexushr.backend.model.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubmitReviewRequest {
    @NotBlank
    private String managerFeedback;

    @Min(1)
    @Max(5)
    private Integer rating;
}
