package com.nexushr.backend.model.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
