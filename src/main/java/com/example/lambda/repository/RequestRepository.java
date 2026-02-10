package com.example.lambda.repository;

import com.example.lambda.model.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA Repository for RequestEntity
 */
@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    
    List<RequestEntity> findByName(String name);
    
    List<RequestEntity> findByStatus(String status);
    
    List<RequestEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
