package com.scalableecommerce.user_service.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scalableecommerce.user_service.model.User;
import com.scalableecommerce.user_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "Register a new user")
  @ApiResponse(responseCode = "201", description = "User registered successfully!")
  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
    User newUser = userService.registerUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
  }

  @GetMapping("/{email}")
  public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
    return userService.findByEmail(email)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<User> findUserById(@PathVariable Long id) {
    try {
      User user = userService.findById(id);
      return ResponseEntity.ok(user);
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @ApiResponse(responseCode = "204", description = "User deleted successfully!")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) { 
    try{
      userService.deleteById(id);
      return ResponseEntity.noContent().build();
    } catch (NoSuchElementException e) { 
      return ResponseEntity.notFound().build();
    }
  }
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUserById(@PathVariable Long id, @Valid @RequestBody User user) {
    try {
      User updatedUser = userService.updateUser(id, user);
      return ResponseEntity.ok(updatedUser);
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

 
}
