package com.BigBangChat.BBC.repository;

import com.BigBangChat.BBC.entities.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<ConversationEntity,Integer> {
}
