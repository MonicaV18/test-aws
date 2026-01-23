package com.example.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

/**
 * AWS Lambda Spring Boot Application
 * 
 * This application serves as an AWS Lambda function using Spring Boot and Java 17.
 * It uses Spring Cloud Function to adapt Spring Boot to AWS Lambda runtime.
 */
@SpringBootApplication
public class LambdaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LambdaApplication.class, args);
    }

    /**
     * Main Lambda function handler
     * 
     * This bean defines a function that processes incoming requests.
     * The function name must match the handler configuration in AWS Lambda.
     * 
     * @return Function that processes Request and returns Response
     */
    @Bean
    public Function<Request, Response> processRequest() {
        return request -> {
            String message = String.format(
                "Hello %s! Your request has been processed successfully.",
                request.getName() != null ? request.getName() : "Guest"
            );
            
            return new Response(
                message,
                "SUCCESS",
                System.currentTimeMillis()
            );
        };
    }
}
