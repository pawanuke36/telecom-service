package com.pawan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawan.dto.SmsRequestDto;
import com.pawan.dto.SmsResponseDto;
import com.pawan.entity.Users;
import com.pawan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.sms-topic:sms_topic}")
    private String smsTopicName;

    public SmsResponseDto sendSms(SmsRequestDto request) {

        Users user = getUser(request.getUsername());
        if (user == null) {
            throw new RuntimeException("No user found");
        }

        // Validate request
        validateSmsRequest(request);

        // Generate acknowledgment ID
        String ackId = UUID.randomUUID().toString();

        try {
            // Create Kafka message
            Map<String, Object> kafkaMessage = createKafkaMessage(ackId, user.getAccountId(), request);
            
            // Send to Kafka
            kafkaTemplate.send(smsTopicName, ackId, objectMapper.writeValueAsString(kafkaMessage));
            
            log.info("SMS request queued successfully with ackId: {}", ackId);
            return SmsResponseDto.success(ackId);
            
        } catch (Exception e) {
            log.error("Failed to queue SMS request", e);
            throw new RuntimeException("Failed to process SMS request", e);
        }
    }

    private Users getUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    private void validateSmsRequest(SmsRequestDto request) {
        if (request.getMobile() == null || request.getMobile().length() != 10) {
            throw new IllegalArgumentException("Invalid mobile number");
        }
        
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        
        if (request.getMessage().length() > 160) {
            throw new IllegalArgumentException("Message exceeds 160 characters");
        }
    }

    private Map<String, Object> createKafkaMessage(String ackId, Integer accountId, SmsRequestDto request) {
        Map<String, Object> message = new HashMap<>();
        message.put("ack_id", ackId);
        message.put("account_id", accountId);
        message.put("mobile", request.getMobile());
        message.put("message", request.getMessage());
        message.put("timestamp", System.currentTimeMillis());
        return message;
    }
}
