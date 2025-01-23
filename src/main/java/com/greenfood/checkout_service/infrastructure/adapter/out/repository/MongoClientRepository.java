package com.greenfood.checkout_service.infrastructure.adapter.out.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.greenfood.checkout_service.infrastructure.adapter.out.entity.CheckoutEntity;

@EnableMongoRepositories
public interface MongoClientRepository extends MongoRepository<CheckoutEntity, String> {
    
}
