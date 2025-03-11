package com.BigBangChat.BBC.service;

import com.BigBangChat.BBC.entities.ConversationEntity;
import com.BigBangChat.BBC.entities.MessageEntity;
import com.BigBangChat.BBC.entities.Role;
import com.BigBangChat.BBC.entities.UserEntity;
import com.BigBangChat.BBC.repository.ConversationRepository;
import com.BigBangChat.BBC.repository.MessageRepository;
import com.BigBangChat.BBC.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    private final RestTemplate restTemplate;

    @Value("${deepseek.api.url}") // Store in application.properties
    private String deepSeekApiUrl;

    @Value("${deepseek.api.key}") // Store in application.properties
    private String deepSeekApiKey;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;


    @Autowired
    public ChatServiceImpl(RestTemplate restTemplate, UserRepository userRepository, ConversationRepository conversationRepository, MessageRepository messageRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
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
    public void sendMessage(Integer conversationId, String text, Role role) {
        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        MessageEntity userMessage = new MessageEntity(text, role, conversation);
        messageRepository.save(userMessage);

        if (role == Role.USER) {
            // Call DeepSeek
            String botResponse = callDeepSeek(text);

            if (botResponse != null && !botResponse.isEmpty()) {
                MessageEntity botMessage = new MessageEntity(botResponse, Role.BOT, conversation);
                messageRepository.save(botMessage);
            }
        }

    }


    @Override
    public List<MessageEntity> getConversation(Integer conversationId) {
        // First verify that the conversation exists
        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // Then retrieve messages
        return messageRepository.findByConversationId(conversationId);
    }
    public String callDeepSeek(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + deepSeekApiKey);
            headers.set("Content-Type", "application/json");

            // Construct the proper request format
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-chat");

            // DeepSeek expects a list of messages
            List<Map<String, String>> messages = Collections.singletonList(
                    Map.of("role", "user", "content", prompt)
            );
            requestBody.put("messages", messages);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(deepSeekApiUrl, HttpMethod.POST, request, String.class);

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // Extract AI response from JSON (assuming response structure)
            return rootNode.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            e.printStackTrace();  // Log error
            return "Error: Unable to process request.";
        }
    }
}
