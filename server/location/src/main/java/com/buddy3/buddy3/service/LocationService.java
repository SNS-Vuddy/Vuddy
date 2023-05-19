package com.buddy3.buddy3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private String receivedMessage = new String();
    ObjectMapper objectMapper = new ObjectMapper();

    public LocationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String key, String value) throws Exception {
        System.out.println("------- 2 --------");
        System.out.println(key + "   :   " + value);
        System.out.println("------- 2 --------");
        kafkaTemplate.send("location-message", key, value);
    }
}