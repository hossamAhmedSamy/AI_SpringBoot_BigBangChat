package com.BigBangChat.BBC.entities;

import jakarta.persistence.*;

@Entity
public class AIResponseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_message_id")
    private MessageEntity userMessage; // Link to the user's message

    @Column(columnDefinition = "TEXT")
    private String deepSeekResponse;

    @Column(columnDefinition = "TEXT")
    private String geminiResponse;

    @Column(columnDefinition = "TEXT")
    private String finalSelectedResponse; // AI-selected best response

    // Constructors
    public AIResponseEntity() {}

    public AIResponseEntity(MessageEntity userMessage, String deepSeekResponse, String geminiResponse) {
        this.userMessage = userMessage;
        this.deepSeekResponse = deepSeekResponse;
        this.geminiResponse = geminiResponse;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer  id) {
        this.id = id;
    }

    public MessageEntity getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(MessageEntity userMessage) {
        this.userMessage = userMessage;
    }

    public String getDeepSeekResponse() {
        return deepSeekResponse;
    }

    public void setDeepSeekResponse(String deepSeekResponse) {
        this.deepSeekResponse = deepSeekResponse;
    }

    public String getGeminiResponse() {
        return geminiResponse;
    }

    public void setGeminiResponse(String geminiResponse) {
        this.geminiResponse = geminiResponse;
    }

    public String getFinalSelectedResponse() {
        return finalSelectedResponse;
    }

    public void setFinalSelectedResponse(String finalSelectedResponse) {
        this.finalSelectedResponse = finalSelectedResponse;
    }
}
