package com.pawan.service;

import com.pawan.entity.Message;
import com.pawan.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public Message saveMessage(Message message) {
        try {
            Message saved = messageRepository.save(message);
            log.debug("Message saved with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Failed to save message", e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Message> getMessagesByStatus(String status) {
        return messageRepository.findByStatusOrderByReceivedAtAsc(status);
    }

    @Transactional
    public void updateMessageStatus(Integer messageId, String status, String telcoResponse) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        
        message.setStatus(status);
        message.setTelcoResponse(telcoResponse);
        
        if ("SENT".equals(status)) {
            message.setSentAt(new Timestamp(System.currentTimeMillis()));
        }
        
        messageRepository.save(message);
    }
}
