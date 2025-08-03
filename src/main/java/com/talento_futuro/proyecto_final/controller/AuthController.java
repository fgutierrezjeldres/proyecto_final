package com.talento_futuro.proyecto_final.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.talento_futuro.proyecto_final.dto.AuthRequest;
import com.talento_futuro.proyecto_final.dto.AuthResponse;
import com.talento_futuro.proyecto_final.service.JwtService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(), request.password()));

        var userDetails = userDetailsService.loadUserByUsername(request.username());
        var token = jwtService.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
    
}
