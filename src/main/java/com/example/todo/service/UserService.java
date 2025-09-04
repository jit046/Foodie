package com.example.todo.service;

import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initializeUsers() {
        // Create default users if they don't exist
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", "admin@example.com", passwordEncoder.encode("admin123"), Set.of("ADMIN"));
            userRepository.save(admin);
            System.out.println("Created admin user: admin/admin123");
        }

        if (!userRepository.existsByUsername("user")) {
            User user = new User("user", "user@example.com", passwordEncoder.encode("user123"), Set.of("USER"));
            userRepository.save(user);
            System.out.println("Created regular user: user/user123");
        }
    }

    public User createUser(String username, String email, String password, Set<String> roles) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User(username, email, passwordEncoder.encode(password), roles);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
