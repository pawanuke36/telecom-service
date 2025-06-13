package com.pawan.controller;

import com.pawan.dto.SmsDeliveryRequest;
import com.pawan.dto.SmsDeliveryResponse;
import com.pawan.service.SmscService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/telco")
@RequiredArgsConstructor
@Slf4j
public class TelecomController {

    private final SmscService smscService;

    @GetMapping("/smsc")
    public ResponseEntity<SmsDeliveryResponse> processSms(
            @RequestParam @NotNull Integer account_id,
            @RequestParam @NotNull String mobile,
            @RequestParam @NotNull String message) {
        
        log.info("Received SMS delivery request - Account: {}, Mobile: {}", 
                account_id, mobile);
        
        try {
            SmsDeliveryRequest request = SmsDeliveryRequest.builder()
                    .accountId(account_id)
                    .mobile(mobile)
                    .message(message)
                    .build();
            
            SmsDeliveryResponse response = smscService.processSmsDelivery(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing SMS delivery", e);
            return ResponseEntity.ok(SmsDeliveryResponse.failure("SYSTEM_ERROR", e.getMessage()));
        }
    }


}
