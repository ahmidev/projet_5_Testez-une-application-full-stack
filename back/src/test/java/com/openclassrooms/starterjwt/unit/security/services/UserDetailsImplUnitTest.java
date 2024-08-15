package com.openclassrooms.starterjwt.unit.security.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

public class UserDetailsImplUnitTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("ahmid.aitouali@laposte.net")
                .firstName("Ahmid")
                .lastName("Ait Ouali")
                .password("password")
                .admin(true)
                .build();
    }

    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities instanceof HashSet);
        assertTrue(authorities.isEmpty());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void testEquals_SameId() {
        UserDetailsImpl otherUser = UserDetailsImpl.builder().id(1L).build();
        assertTrue(userDetails.equals(otherUser));
    }

    @Test
    public void testEquals_DifferentId() {
        UserDetailsImpl otherUser = UserDetailsImpl.builder().id(2L).build();
        assertFalse(userDetails.equals(otherUser));
    }

    @Test
    public void testEquals_Null() {
        assertFalse(userDetails.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(userDetails.equals("A string"));
    }
}
