package com.example.lambda;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the Echo Lambda function.
 */
@SpringBootTest
class EchoLambdaApplicationTest {

    @Autowired
    private Function<String, String> echo;

    @Test
    void contextLoads() {
        assertNotNull(echo, "Echo function bean should be loaded");
    }

    @Test
    void testEchoFunction() {
        String result = echo.apply("World");
        assertEquals("Hello, World", result, "Should return 'Hello, World'");
    }

    @Test
    void testEchoFunctionWithDifferentInput() {
        String result = echo.apply("Lambda");
        assertEquals("Hello, Lambda", result, "Should return 'Hello, Lambda'");
    }

    @Test
    void testEchoFunctionWithEmptyString() {
        String result = echo.apply("");
        assertEquals("Hello, ", result, "Should return 'Hello, '");
    }
}
