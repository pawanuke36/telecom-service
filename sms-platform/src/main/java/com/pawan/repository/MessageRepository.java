package com.pawan.repository;

import com.pawan.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    
    List<Message> findByStatusOrderByReceivedAtAsc(String status);
    
    Optional<Message> findByAckId(String ackId);
    
    @Query("SELECT m FROM Message m WHERE m.status = :status AND m.receivedAt < :timestamp")
    List<Message> findStaleMessagesByStatus(String status, Timestamp timestamp);
}
