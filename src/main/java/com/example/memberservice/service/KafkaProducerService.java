package com.example.memberservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Long> kafkaTemplate;

    public void sendMessage(String topic, Long data) {
        kafkaTemplate.send(topic, data);
    }
}
