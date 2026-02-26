package com.smartcart.inventoryservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil(jwtSecret);
    }

    @Bean
    public JwtFilter jwtFilter(JwtUtil jwtUtil){
        return new JwtFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain  securityFilterChain(HttpSecurity http,JwtFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .anonymous(anonymous->anonymous.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
