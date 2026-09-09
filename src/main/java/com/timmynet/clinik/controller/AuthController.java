package com.timmynet.clinik.controller;

import com.timmynet.clinik.dto.LoginRequest;
import com.timmynet.clinik.dto.LoginResponse;
import com.timmynet.clinik.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${clinik.security.jwt-expiration-ms:3600000}")
    private long expirationMillis;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        String token = jwtService.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(new LoginResponse(token, "Bearer", expirationMillis));
    }
}
