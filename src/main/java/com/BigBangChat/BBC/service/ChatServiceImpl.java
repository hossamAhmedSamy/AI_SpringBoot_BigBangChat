package com.BigBangChat.BBC.service;

import com.BigBangChat.BBC.entities.ConversationEntity;
import com.BigBangChat.BBC.entities.MessageEntity;
import com.BigBangChat.BBC.repository.UserRepository;

import java.util.List;

public class ChatServiceImpl implements ChatService {
    @Override
    public void startConversation(Integer userid) {
        ConversationEntity convo = new ConversationEntity();
        convo.setUser(UserRepository.findById(userid));

    }

    @Override
    public void sendMessage(Integer Conversationid, String message) {

    }

    @Override
    public List<MessageEntity> getConversation(Integer conversationid) {
        return List.of();
    }
}
