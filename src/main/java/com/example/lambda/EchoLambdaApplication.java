package com.example.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

/**
 * Spring Boot application for AWS Lambda deployment.
 * 
 * This application exposes a Function bean that can be invoked by AWS Lambda
 * using the Spring Cloud Function adapter (FunctionInvoker).
 * 
 * The echo function takes a String input and returns a greeting message.
 */
@SpringBootApplication
public class EchoLambdaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EchoLambdaApplication.class, args);
    }

    /**
     * Echo function bean.
     * 
     * When deployed to AWS Lambda, this function can be invoked using:
     * - Handler: org.springframework.cloud.function.adapter.aws.FunctionInvoker
     * - Function name: echo
     * 
     * Input: JSON string (e.g., "World")
     * Output: Greeting message (e.g., "Hello, World")
     * 
     * @return Function that takes a String and returns a greeting
     */
    @Bean
    public Function<String, String> echo() {
        return input -> "Hello, " + input;
    }
}
