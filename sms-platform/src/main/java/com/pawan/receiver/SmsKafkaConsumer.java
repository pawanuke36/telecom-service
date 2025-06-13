package com.pawan.receiver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawan.entity.Message;
import com.pawan.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmsKafkaConsumer {

    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.sms-topic:sms_topic}", groupId = "${spring.kafka.consumer.group-id:sms_group}")
    public void consumeSmsMessage(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        log.info("Received message from topic: {}, partition: {}, offset: {}", topic, partition, offset);
        
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            
            Message smsMessage = Message.builder()
                    .ackId(jsonNode.get("ack_id").asText())
                    .mobile(jsonNode.get("mobile").asText())
                    .message(jsonNode.get("message").asText())
                    .accountId(jsonNode.get("account_id").asInt())
                    .status("NEW")
                    .receivedAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            
            messageService.saveMessage(smsMessage);
            
            // Manual acknowledgment
            acknowledgment.acknowledge();
            
            log.info("Successfully processed message with ackId: {}", smsMessage.getAckId());
            
        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", message, e);
            // Don't acknowledge on error - message will be retried
            throw new RuntimeException("Failed to process message", e);
        }
    }
}
