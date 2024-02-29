package com.hh.testingframwork.kafka.services;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class KafkaTestContainerService implements IKafkaTestContainerService {
    private String brokers;
    KafkaTemplate<String, Object> kafkaTemplate;
    public KafkaTestContainerService(String brokers){
        this.brokers=brokers;
    }
    public void publish(String topicName,Object payload){
        kafkaTemplate=getKafkaTemplate();
        kafkaTemplate.send(topicName,payload);
    }
    private KafkaTemplate<String, Object> getKafkaTemplate() {
        if(kafkaTemplate!=null){
            return kafkaTemplate;
        }
        synchronized (KafkaTestContainerService.class){
            if(kafkaTemplate!=null){
                return kafkaTemplate;
            }else{
               return this.kafkaTemplate=prepareKafkaTemplate();
            }
        }

    }
    private KafkaTemplate<String, Object> prepareKafkaTemplate(){
        DefaultKafkaProducerFactory<String,Object> producerFactory =
                new DefaultKafkaProducerFactory<>(prepareProducerConfigs());
        producerFactory.setValueSerializer(
                new Serializer<Object>() {

                    @Override
                    public void configure(Map<String, ?> configs, boolean isKey) {}

                    @Override
                    public byte[] serialize(String topic, Object data) {

                        ObjectMapper mapper = new ObjectMapper();
                        byte[] bytes = null;
                        try {
                            bytes = mapper.writeValueAsBytes(data);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        return bytes;
                    }

                    @Override
                    public void close() {
                        // Nothing to do

                    }
                });
        return new KafkaTemplate(producerFactory);
    }
    private Map<String, Object> prepareProducerConfigs() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(
                ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        configProps.put(
                ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,1);
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 3000);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 3000);
        return configProps;
    }
}
