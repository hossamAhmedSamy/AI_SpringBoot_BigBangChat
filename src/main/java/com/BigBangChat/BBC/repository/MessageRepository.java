package com.BigBangChat.BBC.repository;

import com.BigBangChat.BBC.entities.MessageEntity;
import com.BigBangChat.BBC.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity,Integer> {
    List<MessageEntity> findByConversationId(Long conversationId);

}
