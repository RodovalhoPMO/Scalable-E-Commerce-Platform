  package com.scalableecommerce.user_service.controller;

  import org.springframework.http.ResponseEntity;
  import org.springframework.security.authentication.AuthenticationManager;
  import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
  import org.springframework.security.core.AuthenticationException;
  import org.springframework.security.crypto.password.PasswordEncoder;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.RequestBody;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;

  import com.scalableecommerce.user_service.model.User;
  import com.scalableecommerce.user_service.repository.UserRepository;
  import com.scalableecommerce.user_service.security.JwtUtil;

  import jakarta.validation.Valid;

  @RestController
  @RequestMapping("/api/auth")
  public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
        AuthenticationManager authenticationManager,
        JwtUtil jwtUtil,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
      this.authenticationManager = authenticationManager;
      this.jwtUtil = jwtUtil;
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
    }
@PostMapping("/login")
public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
    try {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            request.email(), 
            request.password()
        );
        authenticationManager.authenticate(authToken);
        String jwt = jwtUtil.generateToken(request.email());
        return ResponseEntity.ok(new AuthResponse(jwt));
    } catch (AuthenticationException e) {
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
      if (userRepository.existsByEmail(request.email())) {
        return ResponseEntity.badRequest().body("Email already in use");
      }

      User user = new User();
      user.setName(request.name());
      user.setEmail(request.email());
      user.setPassword(passwordEncoder.encode(request.password()));

      userRepository.save(user);
      return ResponseEntity.ok("User registered successfully");
    }
  }

  record AuthRequest(String email, String password) {}
  record AuthResponse(String token) {}
  record RegisterRequest(String name, String email, String password) {}
