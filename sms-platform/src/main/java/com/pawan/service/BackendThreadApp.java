package com.pawan.service;

import com.pawan.entity.Message;
import com.pawan.dto.SmsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackendThreadApp {

    private final MessageService messageService;
    private final RestTemplate restTemplate;
    
    @Value("${app.telco.base-url:http://localhost:8082}")
    private String telcoBaseUrl;

    @Scheduled(fixedRate = 1000) //  every 1 seconds
    public void processNewMessages() {
        log.info("Processing new SMS messages");
        
        try {
            List<Message> newMessages = messageService.getMessagesByStatus("NEW");
            
            if (!newMessages.isEmpty()) {
                log.info("Found {} new messages to process", newMessages.size());
                
                for (Message message : newMessages) {
                    processMessageAsync(message);
                }
            }
            
        } catch (Exception e) {
            log.error("Error in scheduled message processing", e);
        }
    }

    @Async
    public CompletableFuture<Void> processMessageAsync(Message message) {
        log.info("Processing message ID: {}, AckId: {}", message.getId(), message.getAckId());
        
        try {
            // Update status to INPROCESS
            messageService.updateMessageStatus(message.getId(), "INPROCESS", null);
            

            String telcoUrl = String.format("%s/telco/smsc?account_id=%d&mobile=%s&message=%s",
                    telcoBaseUrl,
                    message.getAccountId(),
                    message.getMobile(),
                    URLEncoder.encode(message.getMessage(), StandardCharsets.UTF_8));
            
            SmsResponseDto response = restTemplate.getForObject(telcoUrl, SmsResponseDto.class);
            
            if (response != null && "SUCCESS".equals(response.getResponseCode())) {
                messageService.updateMessageStatus(message.getId(), "SENT", response.getStatus());
                log.info("Message sent successfully: {}", message.getAckId());
            } else {
                messageService.updateMessageStatus(message.getId(), "FAILED", 
                    response != null ? response.getResponseCode() : "NO_RESPONSE");
                log.warn("Message sending failed: {}", message.getAckId());
            }
            
        } catch (Exception e) {
            log.error("Error processing message: {}", message.getAckId(), e);
            messageService.updateMessageStatus(message.getId(), "ERROR", e.getMessage());
        }
        
        return CompletableFuture.completedFuture(null);
    }
}
