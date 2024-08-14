package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByEmailTest() {
        // given - Configuration initiale
        User user = new User();
        user.setEmail("yoga@studio.com");
        user.setPassword("test123!"); // Password encoded
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setAdmin(true);
        userRepository.save(user);

        // when - Action à tester
        boolean existsByEmail = userRepository.existsByEmail("yoga@studio.com");

        // then - Validation du résultat
        assertTrue(existsByEmail, "L'utilisateur avec l'email 'yoga@studio.com' devrait exister dans la base de données");
    }

    @Test
    void findByEmailTest() {
        // given - Configuration initiale
        User user = new User();
        user.setEmail("user@user.com");
        user.setPassword("tes123!"); // Password encoded
        user.setFirstName("User");
        user.setLastName("User");
        user.setAdmin(false);
        userRepository.save(user);

        // when - Action à tester
        Optional<User> foundUser = userRepository.findByEmail("user@user.com");

        // then - Validation du résultat
        assertTrue(foundUser.isPresent(), "L'utilisateur avec l'email 'user@user.com' devrait être trouvé dans la base de données");
        assertEquals("user@user.com", foundUser.get().getEmail());
        assertEquals("User", foundUser.get().getFirstName());
        assertEquals("User", foundUser.get().getLastName());
    }

    @Test
    void deleteUserByIdTest() {
        // given - Configuration initiale
        User user = new User();
        user.setEmail("delete@user.com");
        user.setPassword("test123"); // Password encoded
        user.setFirstName("Delete");
        user.setLastName("User");
        user.setAdmin(false);
        User savedUser = userRepository.save(user);

        // when - Action à tester
        userRepository.deleteById(savedUser.getId());
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());

        // then - Validation du résultat
        assertFalse(deletedUser.isPresent(), "L'utilisateur avec l'ID '" + savedUser.getId() + "' ne devrait plus exister dans la base de données");
    }
}
