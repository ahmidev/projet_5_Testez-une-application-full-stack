package com.openclassrooms.starterjwt.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionService sessionService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testCreateSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga Class");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Morning Yoga Session"); // Ajout de la description ici
        sessionDto.setDate(new Date());

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Class"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testFindSessionById() throws Exception {
        Session session = new Session();
        session.setName("Yoga Class");
        session.setDescription("Morning Yoga Session"); // Ajout de la description ici
        session.setDate(new Date());
        session.setTeacher(new Teacher().setId(1L).setFirstName("John").setLastName("Doe"));

        Session savedSession = sessionService.create(session);

        mockMvc.perform(get("/api/session/" + savedSession.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Class"));
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteSession() throws Exception {
        Session session = new Session();
        session.setName("Yoga Class");
        session.setDescription("Morning Yoga Session");
        session.setDate(new Date());
        session.setTeacher(new Teacher().setId(1L).setFirstName("John").setLastName("Doe"));

        Session savedSession = sessionService.create(session);

        mockMvc.perform(delete("/api/session/" + savedSession.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
