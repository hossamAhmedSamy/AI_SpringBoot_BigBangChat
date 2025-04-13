package com.BigBangChat.BBC.controller;

import com.BigBangChat.BBC.entities.ConversationEntity;
import com.BigBangChat.BBC.entities.UserEntity;
import com.BigBangChat.BBC.repository.UserRepository;
import com.BigBangChat.BBC.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserRepository userRepository;

    // Home page
    @GetMapping("/")
    public String homePage() {
        return "landingPage";
    }

    // Login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Registration page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // Register a new user
    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {

        // Password validation
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        // Create and save new user (simplified, add proper validation and password encryption in production)
        try {
            UserEntity user = new UserEntity();
            user.setName(name);
            user.setMail(email);
            user.setPassword(password); // In production, hash this password
            user.setRole(com.BigBangChat.BBC.entities.Role.USER);

            userRepository.save(user);

            // Start a conversation for the new user
            chatService.startConversation(user.getId());

            // Redirect to login page
            return "redirect:/?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";

        }
    }

    // Handle login (simplified, implement proper authentication in production)
    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, HttpSession session) {
        // In a real application, use Spring Security for authentication
        // For this example, we're doing a simple check

        // Find user by email and password
        Optional<UserEntity> userOpt = userRepository.findByMail(email)
                .filter(user -> user.getPassword().equals(password));
        
        if (userOpt.isPresent()) {
           UserEntity user = userOpt.get();
           session.setAttribute("userId", user.getId());
           return "redirect:/conversationPage";
        } else {
            // User not found or invalid credentials
            return "redirect:/login?error=true";
        }
    }
    
    // conversationPage page
    @GetMapping("/conversationPage")
    public String conversationPage() {
        return "conversationPage";
    }

    // Start a new conversation for a user (REST endpoint)
    @PostMapping("/chat/start/{userId}")
    public ResponseEntity<String> startConversation(@PathVariable Integer userId) {
      ConversationEntity conversation = chatService.startConversation(userId);
      return ResponseEntity.ok("Conversation started with ID: " + conversation.getId());
    }

    // Get the current logged-in user's ID
    @GetMapping("/currentUserId")
    @ResponseBody
    public Integer getCurrentUserId(Authentication authentication) {
        String email = authentication.getName(); // Assuming email is the username
        return userRepository.findByMail(email).map(UserEntity::getId).orElse(null);
    }
}