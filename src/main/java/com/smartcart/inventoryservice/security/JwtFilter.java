package com.smartcart.inventoryservice.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        if (authHeader==null && !authHeader.startsWith("Bearer ")) {
            httpServletResponse(response);
            return;
        }

        String token=authHeader.substring(7);
        Claims claims= jwtUtil.validateToken(token);
        if(claims==null) {
            httpServletResponse(response);
            return;
        }

        List<String> roles=claims.get("roles",List.class);

        Collection<? extends GrantedAuthority> authorities =
                roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .toList();

        var authentication =
                new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), // email
                        null,    //credentials like password but due to jwt it is verified so we dont need
                        authorities
                );

    }
    void httpServletResponse(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        httpServletResponse.getWriter().write("""
            {
                "status": 401,
                "error": "Unauthorized",
                "message": "JWT token is missing or invalid"
            }
        """);

    }
}
