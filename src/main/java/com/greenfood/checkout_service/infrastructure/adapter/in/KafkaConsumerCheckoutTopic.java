package com.greenfood.checkout_service.infrastructure.adapter.in;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfood.checkout_service.infrastructure.adapter.in.model.CheckoutEventKafka;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaConsumerCheckoutTopic {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${kafka.topic.producer.name}")
    private String topicName;

    @Value("${kafka.topic.producer.group-id}")
    private String groupId;

    @KafkaListener(topics = "checkout-topic", groupId = "checkout-group")
    public void listen(String message) { 
        try {
            // Deserializa a mensagem JSON em um objeto CheckoutEvent
            CheckoutEventKafka checkoutEvent = objectMapper.readValue(message, CheckoutEventKafka.class);
            System.out.println("Evento de Checkout recebido: " + checkoutEvent);
            
            // Aqui você pode adicionar a lógica para processar o evento, como salvar no MongoDB
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
