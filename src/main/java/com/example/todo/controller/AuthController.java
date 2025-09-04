package com.example.todo.controller;

import com.example.todo.model.User;
import com.example.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(Authentication authentication) {
        return ResponseEntity.ok("Hello " + (authentication != null ? authentication.getName() : "Guest") + 
                "! Your roles: " + (authentication != null ? authentication.getAuthorities() : "None"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            String password = (String) request.get("password");
            
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Username is required",
                    "error", "MISSING_USERNAME"
                ));
            }
            
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Password is required",
                    "error", "MISSING_PASSWORD"
                ));
            }
            
            // Try to find user
            User user = userService.findByUsername(username);
            
            // For now, we'll just return success since we don't have password verification
            // In a real app, you'd verify the password here
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login successful",
                "user", Map.of(
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "roles", user.getRoles()
                )
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "error", "LOGIN_FAILED"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Login failed: " + e.getMessage(),
                "error", "UNKNOWN_ERROR"
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, Object> request) {
        try {
            // Validate required fields
            String username = (String) request.get("username");
            String email = (String) request.get("email");
            String password = (String) request.get("password");
            
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Username is required",
                    "error", "MISSING_USERNAME"
                ));
            }
            
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email is required",
                    "error", "MISSING_EMAIL"
                ));
            }
            
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Password is required",
                    "error", "MISSING_PASSWORD"
                ));
            }
            
            if (password.length() < 6) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Password must be at least 6 characters long",
                    "error", "WEAK_PASSWORD"
                ));
            }
            
            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid email format",
                    "error", "INVALID_EMAIL"
                ));
            }
            
            Set<String> roles = Set.of(((String) request.getOrDefault("role", "USER")).toUpperCase());
            
            userService.createUser(username, email, password, roles);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User created successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "error", "REGISTRATION_FAILED"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Registration failed: " + e.getMessage(),
                "error", "UNKNOWN_ERROR"
            ));
        }
    }
}
