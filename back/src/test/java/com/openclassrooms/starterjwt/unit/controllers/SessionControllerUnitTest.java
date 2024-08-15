package com.openclassrooms.starterjwt.unit.controllers;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SessionControllerUnitTest {

    private SessionService sessionService;
    private SessionMapper sessionMapper;
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        sessionService = mock(SessionService.class);
        sessionMapper = mock(SessionMapper.class);
        sessionController = new SessionController(sessionService, sessionMapper);
    }

    @Test
    void testFindById() {
        long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);
        SessionDto mockSessionDto = new SessionDto();

        when(sessionService.getById(sessionId)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        ResponseEntity<?> responseEntity = sessionController.findById(String.valueOf(sessionId));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockSessionDto, responseEntity.getBody());
    }

    @Test
    void testFindAll() {
        List<Session> mockSessions = Arrays.asList(new Session(), new Session());
        List<SessionDto> mockSessionDtos = Arrays.asList(new SessionDto(), new SessionDto());

        when(sessionService.findAll()).thenReturn(mockSessions);
        when(sessionMapper.toDto(mockSessions)).thenReturn(mockSessionDtos);

        ResponseEntity<?> responseEntity = sessionController.findAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockSessionDtos, responseEntity.getBody());
    }

    @Test
    void testCreate() {
        SessionDto mockSessionDto = new SessionDto();
        Session mockSession = new Session();

        when(sessionMapper.toEntity(mockSessionDto)).thenReturn(mockSession);
        when(sessionService.create(mockSession)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        ResponseEntity<?> responseEntity = sessionController.create(mockSessionDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockSessionDto, responseEntity.getBody());
    }

    @Test
    void testUpdate() {
        long sessionId = 1L;
        SessionDto mockSessionDto = new SessionDto();
        Session mockSession = new Session();

        when(sessionMapper.toEntity(mockSessionDto)).thenReturn(mockSession);
        when(sessionService.update(sessionId, mockSession)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        ResponseEntity<?> responseEntity = sessionController.update(String.valueOf(sessionId), mockSessionDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockSessionDto, responseEntity.getBody());
    }

    @Test
    void testSave() {
        long sessionId = 1L;

        when(sessionService.getById(sessionId)).thenReturn(new Session());
        doNothing().when(sessionService).delete(sessionId);

        ResponseEntity<?> responseEntity = sessionController.save(String.valueOf(sessionId));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testParticipate() {
        long sessionId = 1L;
        long userId = 1L;

        doNothing().when(sessionService).participate(sessionId, userId);

        ResponseEntity<?> responseEntity = sessionController.participate(String.valueOf(sessionId), String.valueOf(userId));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testNoLongerParticipate() {
        long sessionId = 1L;
        long userId = 1L;

        doNothing().when(sessionService).noLongerParticipate(sessionId, userId);

        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(String.valueOf(sessionId), String.valueOf(userId));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
