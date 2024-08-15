package com.openclassrooms.starterjwt.unit.payload.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import org.junit.jupiter.api.Test;

public class MessageResponseUnitTest {

    @Test
    void getMessageTest() {
        String expectedMessage = "Test message";
        MessageResponse messageResponse = new MessageResponse(expectedMessage);

        String actualMessage = messageResponse.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void setMessageTest() {
        MessageResponse messageResponse = new MessageResponse("Initial message");

        String newMessage = "New message";
        messageResponse.setMessage(newMessage);

        String actualMessage = messageResponse.getMessage();

        assertEquals(newMessage, actualMessage);
    }
}