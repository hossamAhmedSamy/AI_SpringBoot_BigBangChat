/*
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

    // Send a message to a conversation
    @PostMapping("/testselect")
    public String selectBestResponse(@RequestParam String text,
                              @RequestParam String seek,
                              @RequestParam String gemi) {
        chatService.selectBestResponse(text, seek, gemi);
        return "Message sent!";
    }
}

*/

package com.BigBangChat.BBC.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class ChatController {

    // Serve the chat UI for a specific conversation
    @GetMapping("/{conversationId}")
    public String chatPage(@PathVariable Integer conversationId, Model model) {
        model.addAttribute("conversationId", conversationId);
        return "chat"; // Refers to src/main/resources/templates/chat.html
    }
}
