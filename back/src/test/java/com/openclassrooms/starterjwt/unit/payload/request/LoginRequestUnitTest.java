package com.openclassrooms.starterjwt.unit.payload.request;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LoginRequestUnitTest {

    @Test
    public void testGetEmail() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        assertEquals("test@example.com", loginRequest.getEmail());
    }

    @Test
    public void testGetPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password");
        assertEquals("password", loginRequest.getPassword());
    }

    @Test
    public void testSetEmail() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        assertEquals("test@example.com", loginRequest.getEmail());
    }

    @Test
    public void testSetPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password");
        assertEquals("password", loginRequest.getPassword());
    }
}