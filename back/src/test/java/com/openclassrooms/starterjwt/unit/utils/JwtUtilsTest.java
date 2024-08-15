package com.openclassrooms.starterjwt.unit.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.Authentication;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3_600_000); // 1 hour
    }

    @Test
    @DisplayName("Test generating JWT token")
    void generateTokenTest() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .firstName("Toto")
                .lastName("Toto")
                .username("toto3@toto.com")
                .password("test!1234")
                .build();

        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void userNameFromJwtTokenTest() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .password("password")
                .build();

        String validToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000)) // 1 hour
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        String username = jwtUtils.getUserNameFromJwtToken(validToken);

        assertEquals("testUser", username);
    }

    @Test
    void jwtValidateTokenTest() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .password("password")
                .build();

        String validToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000)) // 1 hour
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(validToken);

        assertTrue(isValid);
    }

    @Test
    void validateJwtTokenInvalidSignatureTest() {
        String invalidToken = "invalidToken";

        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void validateJwtTokenMalformedJwtExceptionTest() {
        String invalidToken = "invalidToken";
        JwtUtils jwtUtilsMock = Mockito.mock(JwtUtils.class);
        doThrow(new MalformedJwtException("Invalid JWT token")).when(jwtUtilsMock).validateJwtToken(invalidToken);
        assertThrows(MalformedJwtException.class, () -> jwtUtilsMock.validateJwtToken(invalidToken));
    }

    @Test
    void validateJwtTokenSignatureExceptionTest() {
        JwtUtils jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3_600_000);

        String invalidToken = "invalidToken";
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void validateJwtTokenExpiredJwtExceptionTest() {
        JwtUtils jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3_600_000);

        String expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired token
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        assertFalse(isValid);
    }

    @Test
    void validateJwtTokenIllegalArgumentExceptionTest() {
        try {
            JwtUtils jwtUtils = new JwtUtils();

            String invalidToken = Jwts.builder()
                    .claim(null, jwtUtils)
                    .setSubject("testUser")
                    .signWith(SignatureAlgorithm.HS512, "testSecret")
                    .compact();

            assertThrows(IllegalArgumentException.class,
                    () -> jwtUtils.validateJwtToken(invalidToken));
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Claim property name cannot be null or empty."));
        }
    }
}
