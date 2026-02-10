package com.example.lambda.service;

import com.example.lambda.Request;
import com.example.lambda.Response;
import com.example.lambda.model.RequestDocument;
import com.example.lambda.model.RequestEntity;
import com.example.lambda.repository.RequestDocumentRepository;
import com.example.lambda.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service layer for processing requests with DB and Elasticsearch integration
 */
@Service
public class RequestProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(RequestProcessingService.class);

    @Autowired(required = false)
    private RequestRepository requestRepository;

    @Autowired(required = false)
    private RequestDocumentRepository requestDocumentRepository;

    public Response processRequest(Request request) {
        String name = request.getName() != null ? request.getName() : "Guest";
        String message = String.format(
            "Hello %s! Your request has been processed successfully.",
            name
        );

        // Save to MySQL (if available)
        if (requestRepository != null) {
            try {
                RequestEntity entity = new RequestEntity(name, request.getMessage(), "SUCCESS");
                entity.setProcessedAt(LocalDateTime.now());
                requestRepository.save(entity);
                logger.info("Request saved to database for user: {}", name);
            } catch (Exception e) {
                logger.error("Error saving to database", e);
            }
        } else {
            logger.warn("RequestRepository not available - running in serverless mode");
        }

        // Index in Elasticsearch (if available)
        if (requestDocumentRepository != null) {
            try {
                RequestDocument document = new RequestDocument(
                    name,
                    request.getMessage(),
                    "SUCCESS",
                    message
                );
                requestDocumentRepository.save(document);
                logger.info("Request indexed in Elasticsearch for user: {}", name);
            } catch (Exception e) {
                logger.error("Error indexing in Elasticsearch", e);
            }
        } else {
            logger.warn("RequestDocumentRepository not available - running in serverless mode");
        }

        return new Response(
            message,
            "SUCCESS",
            System.currentTimeMillis()
        );
    }
}
