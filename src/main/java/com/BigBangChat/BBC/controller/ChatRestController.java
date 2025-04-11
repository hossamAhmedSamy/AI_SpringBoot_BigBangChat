package com.BigBangChat.BBC.controller;

import com.BigBangChat.BBC.entities.MessageEntity;
import com.BigBangChat.BBC.entities.Role;
import com.BigBangChat.BBC.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private ChatService chatService;

    // Retrieve messages for a conversation
    @GetMapping("/{conversationId}")
    public List<MessageEntity> getConversation(@PathVariable Integer conversationId) {
        return chatService.getConversation(conversationId);
    }

    // Get messages after a specific timestamp (Uncomment this method)
    @GetMapping("/{conversationId}/after/{timestamp}")
    public List<MessageEntity> getMessagesAfterTimestamp(
            @PathVariable Integer conversationId,
            @PathVariable Long timestamp) {
        LocalDateTime afterTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
        );
        return chatService.getMessagesAfterTimestamp(conversationId, afterTime);
    }

    // Send a message via API
    @PostMapping("/send")
    public void sendMessage(@RequestParam Integer conversationId, @RequestParam String text) {
        CompletableFuture<Void> future = chatService.sendMessage(conversationId, text, Role.USER);

        // Make sure the future completes before returning
        // This is optional - only if you want to wait for the completion
        // future.join();
    }
}