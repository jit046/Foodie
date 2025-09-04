package com.example.todo.config;

import com.example.todo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api/monitoring/**").permitAll() // Allow monitoring endpoints for MCP access
                .requestMatchers("/actuator/**").permitAll() // Allow actuator endpoints for monitoring
                .requestMatchers("/api/todos").hasAnyRole("USER", "ADMIN") // GET todos requires USER or ADMIN role
                .requestMatchers("/api/todos/**").hasRole("ADMIN") // POST, PUT, DELETE require ADMIN role
                .anyRequest().authenticated()
            )
            .userDetailsService(customUserDetailsService) // Configure custom UserDetailsService
            .httpBasic(); // Enable basic authentication for simplicity
        return http.build();
    }
    //This part will encrypt the password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}