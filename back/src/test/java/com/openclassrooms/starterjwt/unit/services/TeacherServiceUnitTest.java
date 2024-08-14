package com.openclassrooms.starterjwt.unittest;

import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceUnitTest {

    @Test
    void findAllTest() {
        // given
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher());
        teachers.add(new Teacher());

        TeacherRepository teacherRepository = mock(TeacherRepository.class);
        when(teacherRepository.findAll()).thenReturn(teachers);

        TeacherService teacherService = new TeacherService(teacherRepository);

        // when
        List<Teacher> result = teacherService.findAll();

        // then
        assertEquals(2, result.size());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findByIdTest() {
        // given
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        TeacherRepository teacherRepository = mock(TeacherRepository.class);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        TeacherService teacherService = new TeacherService(teacherRepository);

        // when
        Teacher result = teacherService.findById(1L);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void findAllWithEmptyListTest() {
        // given
        List<Teacher> teachers = new ArrayList<>();

        TeacherRepository teacherRepository = mock(TeacherRepository.class);
        when(teacherRepository.findAll()).thenReturn(teachers);

        TeacherService teacherService = new TeacherService(teacherRepository);

        // when
        List<Teacher> result = teacherService.findAll();

        // then
        assertEquals(0, result.size());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findByIdNotFoundTest() {
        // given
        TeacherRepository teacherRepository = mock(TeacherRepository.class);
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        TeacherService teacherService = new TeacherService(teacherRepository);

        // when
        Teacher result = teacherService.findById(1L);

        // then
        assertNull(result);
        verify(teacherRepository, times(1)).findById(1L);
    }
}
