package com.openclassrooms.starterjwt.unit.controllers;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthControllerUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerUserWithSuccess() {
        // GIVEN
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("ahmid.aitouali@laposte.net");
        signUpRequest.setFirstName("Ahmid");
        signUpRequest.setLastName("Ait Ouali");
        signUpRequest.setPassword("Test1234");

        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // WHEN
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // THEN
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());

        verify(userRepository, times(1)).existsByEmail("ahmid.aitouali@laposte.net");
        verify(userRepository, times(1)).save(any());
    }
}
