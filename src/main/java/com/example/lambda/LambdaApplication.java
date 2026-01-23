package com.example.lambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

/**
 * Main Spring Boot Application for AWS Lambda Function
 */
@SpringBootApplication
public class LambdaApplication {

    private static final Logger logger = LoggerFactory.getLogger(LambdaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LambdaApplication.class, args);
    }

    /**
     * Simple uppercase function that can be invoked by AWS Lambda
     * Input: String message
     * Output: Uppercase version of the message
     */
    @Bean
    public Function<String, String> uppercase() {
        return value -> {
            if (value == null) {
                logger.warn("Received null input");
                return null;
            }
            logger.info("Input: {}", value);
            String result = value.toUpperCase();
            logger.info("Output: {}", result);
            return result;
        };
    }

    /**
     * Function to reverse a string
     * Input: String message
     * Output: Reversed version of the message
     */
    @Bean
    public Function<String, String> reverse() {
        return value -> {
            if (value == null) {
                logger.warn("Received null input");
                return null;
            }
            logger.info("Input: {}", value);
            String result = new StringBuilder(value).reverse().toString();
            logger.info("Output: {}", result);
            return result;
        };
    }
}
