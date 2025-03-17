package com.BigBangChat.BBC.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime timestamp;


    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    @JsonBackReference
    private ConversationEntity conversation;

    @OneToOne(mappedBy = "userMessage", cascade = CascadeType.ALL)
    private AIResponseEntity aiResponse;



    public MessageEntity() {
    }


    public MessageEntity(String text, Role role, ConversationEntity conversation) {
        this.text = text;
        this.role = role; // Use lowercase 'role'
        this.conversation = conversation;
        this.timestamp = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    public AIResponseEntity getAiResponse() {
        return aiResponse;
    }

    public void setAiResponse(AIResponseEntity aiResponse) {
        this.aiResponse = aiResponse;
    }


}
