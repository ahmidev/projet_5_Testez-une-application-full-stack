package com.openclassrooms.starterjwt.unit.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterUnitTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Test
    void doFilterInternalValidTokenShouldSetAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer validJwtToken");
        when(jwtUtils.validateJwtToken("validJwtToken")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("validJwtToken")).thenReturn("testUser");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).validateJwtToken("validJwtToken");
        verify(jwtUtils, times(1)).getUserNameFromJwtToken("validJwtToken");
        verify(userDetailsService, times(1)).loadUserByUsername("testUser");
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternalInvalidTokenShouldNotSetAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer invalidJwtToken");
        when(jwtUtils.validateJwtToken("invalidJwtToken")).thenReturn(false);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertEquals(null, SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void parseJwtValidHeaderShouldReturnToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testToken");

        String result = authTokenFilter.parseJwt(request);

        assertEquals("testToken", result);
    }

    @Test
    void parseJwtInvalidHeaderShouldReturnNull() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        String result = authTokenFilter.parseJwt(request);

        assertEquals(null, result);
    }
}
