package com.pawan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsResponseDto {
    private String status;
    private String responseCode;
    private String acknowledgementId;
    private String errorMessage;
    
    public static SmsResponseDto success(String ackId) {
        return SmsResponseDto.builder()
                .status("ACCEPTED")
                .responseCode("SUCCESS")
                .acknowledgementId(ackId)
                .build();
    }
    
    public static SmsResponseDto failure(String responseCode, String errorMessage) {
        return SmsResponseDto.builder()
                .status("REJECTED")
                .responseCode(responseCode)
                .errorMessage(errorMessage)
                .build();
    }
}
