package com.example.lambda.repository;

import com.example.lambda.model.RequestDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Elasticsearch Repository for RequestDocument
 */
@Repository
public interface RequestDocumentRepository extends ElasticsearchRepository<RequestDocument, String> {
    
    List<RequestDocument> findByName(String name);
    
    List<RequestDocument> findByStatus(String status);
}
