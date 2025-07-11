package com.scalableecommerce.product_catalog_service.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration-ms}")
  private int jwtExpirationMs;

  private Key key;

  @PostConstruct
  public void init() {
    if (jwtSecret == null || jwtSecret.length() < 32) {
      throw new IllegalArgumentException("JWT secret must be at least 32 characters");
    }

    key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String username) {
    return Jwts.builder()
      .setSubject(username)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public String getUsernameFromJwt(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }

  public boolean validateJwtToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      System.out.println("Token expirado");
    } catch (UnsupportedJwtException e) {
      System.out.println("Token não suportado");
    } catch (MalformedJwtException e) {
      System.out.println("Token malformado");
    } catch (SignatureException e) {
      System.out.println("Assinatura inválida");
    } catch (IllegalArgumentException e) {
      System.out.println("Token vazio ou inválido");
    }

    return false;
  }
}
