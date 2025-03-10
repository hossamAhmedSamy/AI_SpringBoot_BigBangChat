package com.BigBangChat.BBC.service;

import com.BigBangChat.BBC.entities.MessageEntity;

import java.util.List;

public interface ChatService {
    void startConversation(Integer userid);
    void sendMessage(Integer Conversationid ,String message);
    List<MessageEntity> getConversation(Integer conversationid);
}
