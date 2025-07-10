package com.scalableecommerce.user_service.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scalableecommerce.user_service.model.User;
import com.scalableecommerce.user_service.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public UserService(
      UserRepository userRepository
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = new BCryptPasswordEncoder(); // Pode ser injetado via @Bean se preferir
  }

  public User registerUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    User savedUser = userRepository.save(user);
    return savedUser;
  }

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public User findById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found: " + id));
  }

  public void deleteById(Long id) {
    if (!userRepository.existsById(id)) {
      throw new NoSuchElementException("User not found with id: " + id);
    }
    userRepository.deleteById(id);
  }

  public User updateUser(Long id, User updatedUser) {
    User existingUser = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found: " + id));

    existingUser.setName(updatedUser.getName());
    existingUser.setEmail(updatedUser.getEmail());

    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
      existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    }

    return userRepository.save(existingUser);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}
