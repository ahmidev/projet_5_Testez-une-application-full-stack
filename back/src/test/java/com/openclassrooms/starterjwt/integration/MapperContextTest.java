package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.mapper.SessionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MapperContextTest {

    @Autowired
    private SessionMapper sessionMapper;

    @Test
    void contextLoads() {
        assertNotNull(sessionMapper);
    }
}

