package org.example.userserviceapplication.services;

import org.example.userserviceapplication.model.StringValue;
import org.springframework.kafka.core.KafkaTemplate;

public class DataSenderKafka {

    private final KafkaTemplate<String, StringValue> kafkaTemplate;

    private final String topic;

    public DataSenderKafka(KafkaTemplate<String, StringValue> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendMessage(StringValue value) {
        kafkaTemplate.send(topic, value);
    }
}
