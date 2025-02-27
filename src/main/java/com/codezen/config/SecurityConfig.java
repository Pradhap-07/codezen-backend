package com.codezen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/**"))    // ✅ Fix CSRF errors
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // ✅ Allow ALL requests
                )
                .build();
    }
}

