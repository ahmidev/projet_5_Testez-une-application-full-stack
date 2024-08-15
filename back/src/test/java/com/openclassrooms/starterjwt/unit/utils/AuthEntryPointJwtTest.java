package com.openclassrooms.starterjwt.unit.utils;

import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AuthEntryPointJwtTest {

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @Test
    void commenceUnitTest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AuthenticationException("Unauthorized") {};

        authEntryPointJwt.commence(request, response, authException);

        assertEquals(response.getStatus(), HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals(response.getContentType(), MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    @DisplayName("Test de la méthode commence")
    void commenceTest() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/test");

        AuthenticationException authException = mock(AuthenticationException.class);
        when(authException.getMessage()).thenReturn("Unauthorized error");

        HttpServletResponse response = mock(HttpServletResponse.class);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new MockServletOutputStream(byteArrayOutputStream));

        authEntryPointJwt.commence(request, response, authException);

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String expectedBody = "{\"path\":\"/api/test\",\"error\":\"Unauthorized\",\"message\":\"Unauthorized error\",\"status\":401}";

        String actualBody = byteArrayOutputStream.toString("UTF-8");

        assertEquals(expectedBody, actualBody);
    }

    private static class MockServletOutputStream extends ServletOutputStream {
        private final OutputStream outputStream;

        public MockServletOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {
        }
    }
}
