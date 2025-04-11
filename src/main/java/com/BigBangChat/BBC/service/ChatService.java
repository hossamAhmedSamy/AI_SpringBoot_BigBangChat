package com.BigBangChat.BBC.service;

import com.BigBangChat.BBC.entities.ConversationEntity;
import com.BigBangChat.BBC.entities.MessageEntity;
import com.BigBangChat.BBC.entities.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ChatService {
    ConversationEntity startConversation(Integer userid);
    CompletableFuture<Void> sendMessage(Integer Conversationid, String message, Role role);
    List<MessageEntity> getConversation(Integer conversationid);
    String selectBestResponse(String userText, String seekResponse, String gemiResponse);

    // Add this method for timestamp-based fetching
    List<MessageEntity> getMessagesAfterTimestamp(Integer conversationId, LocalDateTime timestamp);
}