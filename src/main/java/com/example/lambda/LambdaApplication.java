package com.example.lambda;

import com.example.lambda.service.RequestProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

/**
 * AWS Lambda Spring Boot Application
 * 
 * This application serves as an AWS Lambda function using Spring Boot and Java 17.
 * It uses Spring Cloud Function to adapt Spring Boot to AWS Lambda runtime.
 * 
 * Features:
 * - PostgreSQL database integration for persistent storage
 * - Elasticsearch integration for indexing and search
 * - Podman/Docker support for local development
 */
@SpringBootApplication
public class LambdaApplication {

    @Autowired
    private RequestProcessingService requestProcessingService;

    public static void main(String[] args) {
        SpringApplication.run(LambdaApplication.class, args);
    }

    /**
     * Main Lambda function handler
     * 
     * This bean defines a function that processes incoming requests.
     * The function name must match the handler configuration in AWS Lambda.
     * 
     * Uses RequestProcessingService to handle DB and Elasticsearch operations.
     * 
     * @return Function that processes Request and returns Response
     */
    @Bean
    public Function<Request, Response> processRequest() {
        return request -> requestProcessingService.processRequest(request);
    }
}
