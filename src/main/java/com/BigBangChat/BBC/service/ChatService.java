package com.BigBangChat.BBC.service;

import com.BigBangChat.BBC.entities.ConversationEntity;
import com.BigBangChat.BBC.entities.MessageEntity;
import com.BigBangChat.BBC.entities.Role;

import java.util.List;

public interface ChatService {
    ConversationEntity startConversation(Integer userid);
    void sendMessage(Integer Conversationid , String message, Role role);
    List<MessageEntity> getConversation(Integer conversationid);
}
