package com.scalableecommerce.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scalableecommerce.user_service.security.JwtUtil;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
    try {
      var authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
      authenticationManager.authenticate(authToken);

      String jwt = jwtUtil.generateToken(request.email());
      return ResponseEntity.ok(new AuthResponse(jwt));
    } catch (AuthenticationException e) {
      return ResponseEntity.status(401).body("Invalid credentials");
    }
  }
  
}

record AuthRequest(String email, String password) {}
record AuthResponse(String token) {}
