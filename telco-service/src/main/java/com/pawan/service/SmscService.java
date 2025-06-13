package com.pawan.service;

import com.pawan.dto.SmsDeliveryRequest;
import com.pawan.dto.SmsDeliveryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmscService {



    public SmsDeliveryResponse processSmsDelivery(SmsDeliveryRequest request) {
        
        // Generate unique message ID
        String messageId = UUID.randomUUID().toString();
        
        // Validate request
        if (!isValidMobileNumber(request.getMobile())) {
            return SmsDeliveryResponse.failure("INVALID_MOBILE", "Invalid mobile number format");
        }
        
        if (request.getMessage().length() > 160) {
            return SmsDeliveryResponse.failure("MESSAGE_TOO_LONG", "Message exceeds 160 characters");
        }

        log.info("SMS accepted for delivery - MessageId: {}, Mobile: {}", messageId, request.getMobile());
        return SmsDeliveryResponse.success(messageId);

    }

    
    private boolean isValidMobileNumber(String mobile) {
        return mobile != null && mobile.matches("^[1-9]\\d{9}$");
    }
    

    

}
