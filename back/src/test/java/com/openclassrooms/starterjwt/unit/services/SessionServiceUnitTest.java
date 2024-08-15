package com.openclassrooms.starterjwt.unit.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void testCreateSession() {
        Session session = new Session();
        session.setName("Yoga");
        session.setDate(new Date());
        session.setDescription("A Yoga session");

        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session createdSession = sessionService.create(session);

        assertNotNull(createdSession);
        assertEquals("Yoga", createdSession.getName());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testFindSessionById() {
        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        session.setName("Yoga");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        Session foundSession = sessionService.getById(sessionId);

        assertNotNull(foundSession);
        assertEquals("Yoga", foundSession.getName());
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    void testParticipateInSession() {
        Long sessionId = 1L;
        Long userId = 1L;

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        sessionService.participate(sessionId, userId);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testNoLongerParticipateInSession() {
        Long sessionId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>(Collections.singletonList(user)));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(sessionId, userId);

        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testDeleteSession() {
        Long sessionId = 1L;

        doNothing().when(sessionRepository).deleteById(sessionId);

        sessionService.delete(sessionId);

        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void testParticipateSessionNotFound() {
        Long sessionId = 1L;
        Long userId = 1L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    void testParticipateUserNotFound() {
        Long sessionId = 1L;
        Long userId = 1L;

        Session session = new Session();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    void testParticipateUserAlreadyParticipating() {
        Long sessionId = 1L;
        Long userId = 1L;

        Session session = new Session();
        User user = new User();
        user.setId(userId);
        session.setUsers(new ArrayList<>(Collections.singletonList(user)));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
    }
}
