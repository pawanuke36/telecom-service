package com.pawan.controller;


import com.pawan.dto.SmsRequestDto;
import com.pawan.dto.SmsResponseDto;
import com.pawan.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sms")
@Validated
@Slf4j
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<SmsResponseDto> sendMessage(@Valid @RequestBody SmsRequestDto request) {
        log.info("Received SMS request for username: {}, mobile: {}", 
                request.getUsername(), request.getMobile());
        
        try {
            SmsResponseDto response = smsService.sendSms(request);
            
            if ("ACCEPTED".equals(response.getStatus())) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (SecurityException e) {
            log.warn("Unauthorized access attempt for username: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(SmsResponseDto.failure("UNAUTHORIZED", null));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(SmsResponseDto.failure("INVALID_REQUEST", null));
        } catch (Exception e) {
            log.error("Error processing SMS request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(SmsResponseDto.failure("INTERNAL_ERROR", null));
        }
    }


}
