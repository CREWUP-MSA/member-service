package com.example.memberservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

	private final KafkaTemplate<String, Long> kafkaTemplate;

	@Value("${spring.kafka.template.default-topic}")
	private String topic;

	public EventProducer(KafkaTemplate<String, Long> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMemberCreateEvent(Long memberId) {
		kafkaTemplate.send(topic, memberId);
	}

}
