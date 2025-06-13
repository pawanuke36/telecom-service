package com.pawan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "ack_id", unique = true, nullable = false)
    private String ackId;
    
    @Column(nullable = false)
    private String mobile;
    
    @Column(nullable = false, length = 160)
    private String message;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "received_at", nullable = false)
    private Timestamp receivedAt;
    
    @Column(name = "sent_at")
    private Timestamp sentAt;
    
    @Column(name = "account_id", nullable = false)
    private Integer accountId;
    
    @Column(name = "telco_response")
    private String telcoResponse;
    
    @Version
    private Long version; // For optimistic locking
}
