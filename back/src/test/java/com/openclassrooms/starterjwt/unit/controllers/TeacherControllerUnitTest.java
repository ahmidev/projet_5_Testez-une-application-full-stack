package com.openclassrooms.starterjwt.unit.controllers;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerUnitTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    @Test
    void findByIdValidIdShouldReturnTeacherDto() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        TeacherDto teacherDto = new TeacherDto(1L, "Doe", "John", null, null);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(ResponseEntity.ok().body(teacherDto), response);
        verify(teacherService, times(1)).findById(1L);
        verify(teacherMapper, times(1)).toDto(teacher);
    }

    @Test
    void findByIdInvalidIdShouldReturnBadRequest() {
        ResponseEntity<?> response = teacherController.findById("invalid");

        assertEquals(ResponseEntity.badRequest().build(), response);
        verify(teacherService, never()).findById(anyLong());
        verify(teacherMapper, never()).toDto(any(Teacher.class));
    }

    @Test
    void findByIdNonExistentTeacherShouldReturnNotFound() {
        when(teacherService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(teacherService, times(1)).findById(1L);
        verify(teacherMapper, never()).toDto(any(Teacher.class));
    }

    @Test
    void findAllShouldReturnListOfTeacherDtos() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher().setId(1L).setFirstName("John").setLastName("Doe"));
        teachers.add(new Teacher().setId(2L).setFirstName("Jane").setLastName("Smith"));

        List<TeacherDto> teacherDtos = new ArrayList<>();
        teacherDtos.add(new TeacherDto(1L, "Doe", "John", null, null));
        teacherDtos.add(new TeacherDto(2L, "Smith", "Jane", null, null));

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        ResponseEntity<?> response = teacherController.findAll();

        assertEquals(ResponseEntity.ok().body(teacherDtos), response);
        verify(teacherService, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(teachers);
    }
}
