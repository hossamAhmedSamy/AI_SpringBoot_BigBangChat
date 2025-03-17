package com.BigBangChat.BBC.service;

import com.BigBangChat.BBC.entities.*;
import com.BigBangChat.BBC.repository.AIResponseRepository;
import com.BigBangChat.BBC.repository.ConversationRepository;
import com.BigBangChat.BBC.repository.MessageRepository;
import com.BigBangChat.BBC.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ChatServiceImpl implements ChatService {

    private final RestTemplate restTemplate;

    @Value("${deepseek.api.url}") // Store in application.properties
    private String deepSeekApiUrl;

    @Value("${deepseek.api.key}") // Store in application.properties
    private String deepSeekApiKey;

    @Value("${gemini.api.url}") // Store in application.properties
    private String geminiApiUrl;

    @Value("${gemini.api.key}") // Store in application.properties
    private String geminiApiKey;


    @Autowired
    private AIResponseRepository aiResponseRepository;


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
    @Transactional
    @Override
    public CompletableFuture<Void> sendMessage(Integer conversationId, String text, Role role) {
        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        MessageEntity userMessage = new MessageEntity(text, role, conversation);
        messageRepository.save(userMessage);

        if (role == Role.USER) {
            // Start asynchronous API calls
            CompletableFuture<String> seekFuture = CompletableFuture.supplyAsync(() -> callDeepSeek(text));
            CompletableFuture<String> gemiFuture = CompletableFuture.supplyAsync(() -> callGemini(text));

            return seekFuture.thenCombine(gemiFuture, (seekResponse, gemiResponse) -> {
                        // Handle API failures
                        String finalSeek = (seekResponse != null) ? seekResponse : "DeepSeek failed.";
                        String finalGemi = (gemiResponse != null) ? gemiResponse : "Gemini failed.";

                        // Save AI responses
                        AIResponseEntity aiResponse = new AIResponseEntity(userMessage, finalSeek, finalGemi);
                        aiResponseRepository.save(aiResponse);

                        // **Step 2: Select the best response asynchronously**
                        return CompletableFuture.supplyAsync(() -> selectBestResponse(text, finalSeek, finalGemi))
                                .thenAccept(bestResponse -> {
                                    // Ensure AI has completed selection
                                    System.out.println("-=============sosososo : "+bestResponse);
                                    if (bestResponse == null || bestResponse.isEmpty()) {
                                        bestResponse = "No suitable response found.";
                                        System.out.println(bestResponse);
                                    }

                                    // Save bot response
                                    MessageEntity botMessage = new MessageEntity(bestResponse, Role.BOT, conversation);
                                    messageRepository.save(botMessage);

                                    // Update AIResponseEntity with the selected response
                                    aiResponse.setFinalSelectedResponse(bestResponse);
                                    aiResponseRepository.save(aiResponse);
                                });
                    }).thenCompose(future -> future) // Ensures correct async flow
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });
        }

        return CompletableFuture.completedFuture(null);
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
    public String callGemini(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construct the request body
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> contents = new HashMap<>();
            Map<String, String> parts = new HashMap<>();

            parts.put("text", prompt);
            contents.put("parts", List.of(parts));
            requestBody.put("contents", List.of(contents));

            // Append API key as a query parameter
            String urlWithApiKey = geminiApiUrl + "?key=" + geminiApiKey;

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(urlWithApiKey, HttpMethod.POST, request, String.class);

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // Extract AI response from JSON
            return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
        } catch (Exception e) {
            e.printStackTrace();  // Log error
            return "Error: Unable to process request.";
        }
    }
    public String selectBestResponse(String userText, String seekResponse, String gemiResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construct the Ollama prompt
        String prompt = "A user sent the following message:\n"
                + "User: " + userText + "\n\n"
                + "Two AI models responded:\n"
                + "Response 1: " + seekResponse + "\n"
                + "Response 2: " + gemiResponse + "\n\n"
                + "Your task: Select the best response based on clarity, relevance, and completeness. "
                + "If necessary, merge the two responses into a single, better response. "
                + "Return only the final chosen response.";

        // Create the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-r1:7b"); // Your Ollama model
        requestBody.put("prompt", prompt);
        requestBody.put("stream", false); // Disable streaming

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            // Call Ollama API
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:11434/api/generate", // Ollama endpoint
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // Parse JSON response safely
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // Extract AI-selected response
            return rootNode.path("response").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error: Unable to process AI response.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: AI selection failed.";
        }
    }



}
