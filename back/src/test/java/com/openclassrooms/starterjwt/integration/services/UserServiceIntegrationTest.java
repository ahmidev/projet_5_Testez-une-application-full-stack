package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        // création d'un utilisateur
        User specificUser = new User();
        specificUser.setId(1L); // Assurez-vous que l'ID est unique ou géré automatiquement par votre base de données
        specificUser.setEmail("ahmid.aitouali@laposte.net");
        specificUser.setFirstName("Ahmid");
        specificUser.setLastName("Ait Ouali");
        specificUser.setAdmin(false);
        specificUser.setPassword(passwordEncoder.encode("password123"));

        // Enregistrement de l'utilisateur en bdd
        userRepository.saveAndFlush(specificUser);
    }

    @AfterEach
    void tearDown() {
        // Nettoyage après chaque test
        userRepository.deleteAll();
    }

    @Test
    void testFindById_SpecificUser() {
        // Test de la récupération de l'utilisateur ajouté dans setUp()
        Optional<User> user = userRepository.findByEmail("ahmid.aitouali@laposte.net");
        assertTrue(user.isPresent(), "L'utilisateur ahmid.aitouali@laposte.net devrait exister");

        User foundUser = userService.findById(user.get().getId());
        assertNotNull(foundUser);
        assertEquals("ahmid.aitouali@laposte.net", foundUser.getEmail());
        assertEquals("Ahmid", foundUser.getFirstName());
        assertEquals("Ait Ouali", foundUser.getLastName());
    }

    @Test
    void testFindById_NonExistentUser() {
        // Test d'un utilisateur inexistant
        Long nonExistentUserId = 999L;
        User user = userService.findById(nonExistentUserId);
        assertNull(user, "L'utilisateur ne devrait pas exister dans la base de données");
    }

    @Test
    void testDeleteById_Success() {
        // Test de la suppression d'un utilisateur
        Optional<User> user = userRepository.findByEmail("ahmid.aitouali@laposte.net");
        assertTrue(user.isPresent(), "L'utilisateur ahmid.aitouali@laposte.net devrait exister");

        userService.delete(user.get().getId());

        Optional<User> deletedUser = userRepository.findById(user.get().getId());
        assertFalse(deletedUser.isPresent(), "L'utilisateur devrait avoir été supprimé");
    }
}
