package com.hh.testingframwork.kafka;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class KafkaStartupExtension implements BeforeAllCallback, AfterAllCallback {
    public static KafkaContainer kafkaContainer;

    public KafkaStartupExtension(){
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    }

    @Override
    public void beforeAll(ExtensionContext context) {
        if(!kafkaContainer.isRunning()){
            kafkaContainer.start();
        }
        log.info("Set wiremock url fps.testcontainer.kafka.url : ", kafkaContainer.getBootstrapServers());
        System.setProperty("fps.testcontainer.kafka.url", kafkaContainer.getHost()+":"+kafkaContainer.getFirstMappedPort());
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if(kafkaContainer!=null && kafkaContainer.isRunning()){
            //kafkaContainer.stop();
        }
    }
}
