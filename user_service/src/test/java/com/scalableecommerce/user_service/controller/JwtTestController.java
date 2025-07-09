package com.scalableecommerce.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scalableecommerce.user_service.security.JwtUtil;

@RestController
public class JwtTestController {

    @Autowired
    private JwtUtil jwtUtil;

    // Exemplo: gerar token passando o username como parâmetro
    @GetMapping("/generate-token")
    public String generateToken(@RequestParam String username) {
        return jwtUtil.generateToken(username);
    }
}
