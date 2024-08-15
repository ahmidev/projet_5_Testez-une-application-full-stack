package com.openclassrooms.starterjwt.unit.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperUnitTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testToEntity() {
        // Given
        UserDto userDTO = new UserDto();
        userDTO.setId(1L);
        userDTO.setEmail("ahmid.aitouali@laposte.net");
        userDTO.setLastName("Ait Ouali");
        userDTO.setFirstName("Ahmid");
        userDTO.setPassword("password");
        userDTO.setAdmin(true);

        // When
        User user = userMapper.toEntity(userDTO);

        // Then
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getLastName(), user.getLastName());
        assertEquals(userDTO.getFirstName(), user.getFirstName());
        assertEquals(userDTO.getPassword(), user.getPassword());
        assertEquals(userDTO.isAdmin(), user.isAdmin());
    }

    @Test
    public void testToDto() {
        // Given
        User user = new User(1L, "ahmid.aitouali@laposte.net", "Ait Ouali", "Ahmid", "password", true, null, null);

        // When
        UserDto userDTO = userMapper.toDto(user);

        // Then
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getLastName(), userDTO.getLastName());
        assertEquals(user.getFirstName(), userDTO.getFirstName());
        assertEquals(user.getPassword(), userDTO.getPassword());
        assertEquals(user.isAdmin(), userDTO.isAdmin());
    }
}
