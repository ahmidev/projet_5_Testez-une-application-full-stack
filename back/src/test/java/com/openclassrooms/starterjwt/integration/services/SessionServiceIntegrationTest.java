package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SessionServiceIntegrationTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionService sessionService;

    private Session testSession;

    @BeforeEach
    void setUp() {
        // Création d'une session pour les tests
        testSession = new Session();
        testSession.setName("Yoga");
        testSession.setDate(new Date());
        testSession.setDescription("A Yoga session");
        sessionRepository.saveAndFlush(testSession);
    }

    @AfterEach
    void tearDown() {
        // Nettoyage des données après chaque test
        sessionRepository.deleteAll();
    }

    @Test
    void testCreateSession() {
        Session newSession = new Session();
        newSession.setName("Pilates");
        newSession.setDate(new Date());
        newSession.setDescription("A Pilates session");

        Session createdSession = sessionService.create(newSession);

        assertNotNull(createdSession);
        assertEquals("Pilates", createdSession.getName());
        assertNotNull(createdSession.getId());
    }

    @Test
    void testFindSessionById() {
        Optional<Session> foundSession = sessionRepository.findById(testSession.getId());

        assertTrue(foundSession.isPresent());
        assertEquals("Yoga", foundSession.get().getName());
    }

    @Test
    void testDeleteSession() {
        sessionService.delete(testSession.getId());

        Optional<Session> deletedSession = sessionRepository.findById(testSession.getId());
        assertFalse(deletedSession.isPresent());
    }
}
