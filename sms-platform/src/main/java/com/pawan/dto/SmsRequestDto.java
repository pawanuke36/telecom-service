package com.pawan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequestDto {
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @NotNull(message = "Mobile number is required")
    @Pattern(regexp = "^[1-9]\\d{9}$", message = "Invalid mobile number format")
    private String mobile;
    
    @NotBlank(message = "Message is required")
    @Size(max = 160, message = "Message cannot exceed 160 characters")
    private String message;
}
