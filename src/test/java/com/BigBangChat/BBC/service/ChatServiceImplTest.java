package com.BigBangChat.BBC.service;

import com.BigBangChat.BBC.entities.ConversationEntity;
import com.BigBangChat.BBC.entities.MessageEntity;
import com.BigBangChat.BBC.entities.Role;
import com.BigBangChat.BBC.entities.UserEntity;
import com.BigBangChat.BBC.repository.ConversationRepository;
import com.BigBangChat.BBC.repository.MessageRepository;
import com.BigBangChat.BBC.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest  // Uses an in-memory database for testing
public class ChatServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    private ChatService chatService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        chatService = new ChatServiceImpl(userRepository, conversationRepository, messageRepository);

        // Create and save a test user
        testUser = new UserEntity();
        testUser.setName("testuser");
        userRepository.save(testUser);
    }

    @Test
    void testStartConversation_Success() {
        // Start a conversation for the test user
        chatService.startConversation(testUser.getId());

        // Retrieve conversation from the database
        List<ConversationEntity> conversations = conversationRepository.findAll();
        assertFalse(conversations.isEmpty(), "Conversation should be created!");
        assertEquals(testUser.getId(), conversations.get(0).getUser().getId(), "User should match!");
    }

    @Test
    void testStartConversation_UserNotFound() {
        int nonExistingUserId = 999;
        Exception exception = assertThrows(RuntimeException.class, () -> {
            chatService.startConversation(nonExistingUserId);
        });

        assertEquals("User with ID " + nonExistingUserId + " not found", exception.getMessage());
    }

    @Test
    void testSendMessage_Success() {
        // Start a conversation
        chatService.startConversation(testUser.getId());
        ConversationEntity conversation = conversationRepository.findAll().get(0);

        // Send a message
        chatService.sendMessage(conversation.getId(), "Hello, bot!", Role.USER);

        // Verify message was saved
        List<MessageEntity> messages = messageRepository.findAll();
        assertFalse(messages.isEmpty(), "Message should be stored!");
        assertEquals("Hello, bot!", messages.get(0).getText(), "Message text should match!");
        assertEquals(Role.USER, messages.get(0).getRole(), "Message role should match!");
        assertEquals(conversation.getId(), messages.get(0).getConversation().getId(), "Message should belong to conversation!");
    }

    @Test
    void testSendMessage_ConversationNotFound() {
        int nonExistingConversationId = 999;
        Exception exception = assertThrows(RuntimeException.class, () -> {
            chatService.sendMessage(nonExistingConversationId, "Hello!", Role.USER);
        });

        assertEquals("Conversation not found", exception.getMessage());
    }

    @Test
    void testGetConversationMessages() {
        // Start a conversation and send multiple messages
        chatService.startConversation(testUser.getId());
        ConversationEntity conversation = conversationRepository.findAll().get(0);

        chatService.sendMessage(conversation.getId(), "Message 1", Role.USER);
        chatService.sendMessage(conversation.getId(), "Message 2", Role.BOT);

        // Retrieve messages
        List<MessageEntity> messages = chatService.getConversation(conversation.getId());

        assertEquals(2, messages.size(), "Conversation should have 2 messages!");
        assertEquals("Message 1", messages.get(0).getText());
        assertEquals("Message 2", messages.get(1).getText());
    }

    @Test
    void testGetConversationMessages_Empty() {
        // Start a conversation without messages
        chatService.startConversation(testUser.getId());
        ConversationEntity conversation = conversationRepository.findAll().get(0);

        List<MessageEntity> messages = chatService.getConversation(conversation.getId());

        assertTrue(messages.isEmpty(), "New conversation should have no messages!");
    }

    @Test
    void testGetConversationMessages_ConversationNotFound() {
        int nonExistingConversationId = 999;
        Exception exception = assertThrows(RuntimeException.class, () -> {
            chatService.getConversation(nonExistingConversationId);
        });

        assertEquals("Conversation not found", exception.getMessage());
    }
}
