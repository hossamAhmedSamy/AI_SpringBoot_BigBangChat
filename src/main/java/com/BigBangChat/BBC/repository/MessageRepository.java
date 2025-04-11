package com.BigBangChat.BBC.repository;

import com.BigBangChat.BBC.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
    List<MessageEntity> findByConversationId(Integer conversationId);

    // New method to find messages after timestamp
    List<MessageEntity> findByConversationIdAndTimestampAfter(
            Integer conversationId,
            LocalDateTime timestamp
    );
}