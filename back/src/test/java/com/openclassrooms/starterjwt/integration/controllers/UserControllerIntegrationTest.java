package com.openclassrooms.starterjwt.integration.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TeacherMapper teacherMapper;


    @Test
    @WithMockUser(username = "ahmid.aitouali@laposte.net", roles = "ADMIN")
    void testFindById_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("ahmid.aitouali@laposte.net");
        user.setLastName("Ait Ouali");
        user.setFirstName("Ahmid");

        UserDto userDto = new UserDto();
        userDto.setEmail("ahmid.aitouali@laposte.net");
        userDto.setLastName("Ait Ouali");
        userDto.setFirstName("Ahmid");

        when(userService.findById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ahmid.aitouali@laposte.net"))
                .andExpect(jsonPath("$.lastName").value("Ait Ouali")); // Ajoutez cette assertion
    }

}
