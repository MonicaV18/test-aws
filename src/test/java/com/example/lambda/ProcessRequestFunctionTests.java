package com.example.lambda;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProcessRequestFunctionTests {

    @Autowired
    private Function<Request, Response> processRequest;

    @Test
    void testProcessRequestWithName() {
        // Arrange
        Request request = new Request("John Doe", "Test message");

        // Act
        Response response = processRequest.apply(request);

        // Assert
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertTrue(response.getMessage().contains("John Doe"));
        assertTrue(response.getTimestamp() > 0);
    }

    @Test
    void testProcessRequestWithoutName() {
        // Arrange
        Request request = new Request(null, "Test message");

        // Act
        Response response = processRequest.apply(request);

        // Assert
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertTrue(response.getMessage().contains("Guest"));
        assertTrue(response.getTimestamp() > 0);
    }

    @Test
    void testProcessRequestEmptyRequest() {
        // Arrange
        Request request = new Request();

        // Act
        Response response = processRequest.apply(request);

        // Assert
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertTrue(response.getMessage().contains("Guest"));
    }
}
