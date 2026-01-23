package com.example.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

/**
 * Main Spring Boot Application for AWS Lambda Function
 */
@SpringBootApplication
public class LambdaApplication {

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
            System.out.println("Input: " + value);
            String result = value.toUpperCase();
            System.out.println("Output: " + result);
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
            System.out.println("Input: " + value);
            String result = new StringBuilder(value).reverse().toString();
            System.out.println("Output: " + result);
            return result;
        };
    }
}
