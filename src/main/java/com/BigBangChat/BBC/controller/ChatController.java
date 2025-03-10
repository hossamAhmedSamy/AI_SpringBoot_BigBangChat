package com.BigBangChat.BBC.controller;

import com.BigBangChat.BBC.entities.MessageEntity;
import com.BigBangChat.BBC.entities.Role;
import com.BigBangChat.BBC.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // Start a new conversation for a user
    @PostMapping("/start/{userId}")
    public String startConversation(@PathVariable Integer userId) {
        chatService.startConversation(userId);
        return "Conversation started for user ID: " + userId;
    }

    // Send a message to a conversation
    @PostMapping("/send")
    public String sendMessage(@RequestParam Integer conversationId,
                              @RequestParam String text,
                              @RequestParam Role role) {
        chatService.sendMessage(conversationId, text, role);
        return "Message sent!";
    }

    // Retrieve all messages of a conversation
    @GetMapping("/{conversationId}")
    public List<MessageEntity> getConversation(@PathVariable Integer conversationId) {
        return chatService.getConversation(conversationId);
    }
}
