package com.hh.testingframwork.kafka.services;

public interface IKafkaTestContainerService {
     void publish(String topicName,Object payload);
}
