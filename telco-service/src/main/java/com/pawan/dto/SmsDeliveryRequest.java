package com.pawan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsDeliveryRequest {
    
    @NotNull(message = "Account ID is required")
    private Integer accountId;
    
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[1-9]\\d{9}$", message = "Invalid mobile number format")
    private String mobile;
    
    @NotBlank(message = "Message is required")
    private String message;
    
    private String priority = "NORMAL"; // HIGH, NORMAL, LOW
    private String messageType = "TEXT"; // TEXT, FLASH, BINARY
}
