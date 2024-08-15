package com.openclassrooms.starterjwt.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionMapperUnitTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    @Test
    public void testToEntity() {
        // GIVEN
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Test description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(List.of(1L, 2L, 3L));

        when(teacherService.findById(1L)).thenReturn(new Teacher().setFirstName("Ahmid").setLastName("Ait Ouali"));
        when(userService.findById(1L)).thenReturn(new User());
        when(userService.findById(2L)).thenReturn(new User());
        when(userService.findById(3L)).thenReturn(new User());

        // WHEN
        Session session = sessionMapper.toEntity(sessionDto);

        // THEN
        assertEquals("Test description", session.getDescription());
        assertEquals(3, session.getUsers().size());
        assertEquals("Ahmid", session.getTeacher().getFirstName());
        assertEquals("Ait Ouali", session.getTeacher().getLastName());
    }

    @Test
    public void testToDto() {
        // GIVEN
        Session session = new Session();
        session.setDescription("Test description");
        session.setTeacher(new Teacher().setFirstName("Ahmid").setLastName("Ait Ouali"));
        session.setUsers(List.of(new User(), new User(), new User()));

        // WHEN
        SessionDto sessionDto = sessionMapper.toDto(session);

        // THEN
        assertEquals("Test description", sessionDto.getDescription());
        assertEquals(3, sessionDto.getUsers().size());
    }
}
