package com.BigBangChat.BBC.service;

import com.BigBangChat.BBC.entities.ConversationEntity;
import com.BigBangChat.BBC.entities.MessageEntity;
import com.BigBangChat.BBC.entities.Role;
import com.BigBangChat.BBC.entities.UserEntity;
import com.BigBangChat.BBC.repository.ConversationRepository;
import com.BigBangChat.BBC.repository.MessageRepository;
import com.BigBangChat.BBC.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    public ChatServiceImpl(UserRepository userRepository, ConversationRepository conversationRepository, MessageRepository messageRepository) {
    }

    @Override
    public ConversationEntity startConversation(Integer userId) {
        // Find user or throw an exception
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        // Create and save a new conversation
        ConversationEntity conversation = new ConversationEntity();
        conversation.setUser(user);
        return conversationRepository.save(conversation);
    }

    @Override
    public MessageEntity sendMessage(Integer conversationId, String text, Role role) {
        // Retrieve conversation or throw an exception
        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // Create and save the message
        MessageEntity message = new MessageEntity(text, role, conversation);
        return messageRepository.save(message);
    }

    @Override
    public List<MessageEntity> getConversation(Integer conversationId) {
        // Retrieve messages belonging to a conversation
        return messageRepository.findByConversationId(conversationId);
    }
}
