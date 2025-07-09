package com.scalableecommerce.user_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  @NotBlank(message = "Email is mandatory")
  @Email(message = "Invalid Email")
  private String email;

  @Column(nullable = false)
  @NotBlank(message = "Senha é obrigatória")
  @Size(min = 6, message = "Senha deve ter ao menos 6 caracteres")
  private String password;

  private String name;
}
