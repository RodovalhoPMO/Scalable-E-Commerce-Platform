package com.scalableecommerce.user_service.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.scalableecommerce.user_service.model.User;
import com.scalableecommerce.user_service.repository.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository);
    }

    @Test 
    void testRegisterUser_passwordIsEncoded() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("123456");

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User savedUser = userService.registerUser(user);

        assertNotEquals("123456", savedUser.getPassword(), "Password should be encoded");
        assertTrue(passwordEncoder.matches("123456", savedUser.getPassword()));
        verify(userRepository, times(1)).save(savedUser);
    }
}
