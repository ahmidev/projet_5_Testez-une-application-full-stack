package com.openclassrooms.starterjwt.integration.repository;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@Transactional
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void existsByEmailTest() {
        // given
        if (!userRepository.existsByEmail("yoga@studio.com")) {
            User user = new User();
            user.setEmail("yoga@studio.com");
            user.setPassword("test123!"); // Password encoded
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setAdmin(true);
            userRepository.save(user);
        }

        // when
        boolean existsByEmail = userRepository.existsByEmail("yoga@studio.com");

        // then
        assertTrue(existsByEmail, "L'utilisateur avec l'email 'yoga@studio.com' devrait exister dans la base de données");
    }


    @Test
    void findByEmailTest() {
        // given
        User user = new User();
        user.setEmail("usertest@usertest.com");
        user.setPassword("tes123!"); // Password encoded
        user.setFirstName("User");
        user.setLastName("User");
        user.setAdmin(false);
        userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findByEmail("usertest@usertest.com");

        // then
        assertTrue(foundUser.isPresent(), "L'utilisateur avec l'email 'user@user.com' devrait être trouvé dans la base de données");
        assertEquals("usertest@usertest.com", foundUser.get().getEmail());
        assertEquals("User", foundUser.get().getFirstName());
        assertEquals("User", foundUser.get().getLastName());
    }

    @Test
    void deleteUserByIdTest() {
        // given
        User user = new User();
        user.setEmail("delete@user.com");
        user.setPassword("test123"); // Password encoded
        user.setFirstName("Delete");
        user.setLastName("User");
        user.setAdmin(false);
        User savedUser = userRepository.save(user);

        // when
        userRepository.deleteById(savedUser.getId());
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());

        // then
        assertFalse(deletedUser.isPresent(), "L'utilisateur avec l'ID '" + savedUser.getId() + "' ne devrait plus exister dans la base de données");
    }
}
