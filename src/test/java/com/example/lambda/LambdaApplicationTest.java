package com.example.lambda;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test class for Lambda functions
 */
@SpringBootTest
class LambdaApplicationTest {

    @Autowired
    private Function<String, String> uppercase;

    @Autowired
    private Function<String, String> reverse;

    @Test
    void testUppercaseFunction() {
        assertNotNull(uppercase);
        String input = "hello world";
        String expected = "HELLO WORLD";
        String result = uppercase.apply(input);
        assertEquals(expected, result);
    }

    @Test
    void testReverseFunction() {
        assertNotNull(reverse);
        String input = "hello";
        String expected = "olleh";
        String result = reverse.apply(input);
        assertEquals(expected, result);
    }

    @Test
    void testUppercaseWithEmptyString() {
        String result = uppercase.apply("");
        assertEquals("", result);
    }

    @Test
    void testReverseWithSingleCharacter() {
        String result = reverse.apply("a");
        assertEquals("a", result);
    }

    @Test
    void testUppercaseWithNull() {
        String result = uppercase.apply(null);
        assertNull(result);
    }

    @Test
    void testReverseWithNull() {
        String result = reverse.apply(null);
        assertNull(result);
    }
}
