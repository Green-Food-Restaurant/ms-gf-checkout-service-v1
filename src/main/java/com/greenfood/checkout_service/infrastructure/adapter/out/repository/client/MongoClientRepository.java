package com.greenfood.checkout_service.infrastructure.adapter.out.repository.client;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.greenfood.checkout_service.infrastructure.adapter.out.repository.entity.CheckoutEntity;

@EnableMongoRepositories
public interface MongoClientRepository extends MongoRepository<CheckoutEntity, String> {

    Optional<CheckoutEntity> findByCartId(String cartId);
}
