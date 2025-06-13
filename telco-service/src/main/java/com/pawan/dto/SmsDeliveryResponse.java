package com.pawan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsDeliveryResponse {
    private String responseCode;
    private String status;
    private String messageId;
    private String errorMessage;
    private LocalDateTime timestamp;

    public static SmsDeliveryResponse success(String messageId) {
        return SmsDeliveryResponse.builder()
                .responseCode("SUCCESS")
                .status("ACCEPTED")
                .messageId(messageId)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static SmsDeliveryResponse failure(String responseCode, String errorMessage) {
        return SmsDeliveryResponse.builder()
                .responseCode(responseCode)
                .status("REJECTED")
                .errorMessage(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static SmsDeliveryResponse delivered(String messageId) {
        return SmsDeliveryResponse.builder()
                .responseCode("SUCCESS")
                .status("DELIVERED")
                .messageId(messageId)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static SmsDeliveryResponse pending(String messageId) {
        return SmsDeliveryResponse.builder()
                .responseCode("SUCCESS")
                .status("PENDING")
                .messageId(messageId)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
