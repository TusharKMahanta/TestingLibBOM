package com.hh.testingframwork.couchbase;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;

import java.time.Duration;
@Slf4j
public class CouchbaseStartupExtension implements BeforeAllCallback, AfterAllCallback {
    public static CouchbaseContainer couchbaseContainer;

    public CouchbaseStartupExtension(String bucket){
        couchbaseContainer =
                new CouchbaseContainer("couchbase/server:7.1.4")
                        .withBucket(new BucketDefinition(bucket))
                        .withCredentials(bucket, bucket)
                        .withStartupAttempts(5)
                        .withStartupTimeout(Duration.ofMinutes(10))
                        .waitingFor(Wait.defaultWaitStrategy());
    }
    public CouchbaseStartupExtension(String bucket,String bucketUserName,String bucketPassword){
        couchbaseContainer =
                new CouchbaseContainer("couchbase/server:7.1.4")
                        .withBucket(new BucketDefinition(bucket))
                        .withCredentials(bucketUserName, bucketPassword)
                        .withStartupAttempts(5)
                        .withStartupTimeout(Duration.ofMinutes(10))
                        .waitingFor(Wait.defaultWaitStrategy());
    }
    public CouchbaseStartupExtension(CouchbaseContainer couchbaseContainer){
        this.couchbaseContainer=couchbaseContainer;
    }
    @Override
    public void beforeAll(ExtensionContext context) {
        if(!couchbaseContainer.isRunning()){
            log.info("Starting Couchbase container for the first time");
            couchbaseContainer.start();
            log.info("Couchbase container started");
        }
        log.info("Couchbase connection string = {}", couchbaseContainer.getConnectionString());
        System.setProperty("fps.testcontainer.couchbase.url", couchbaseContainer.getConnectionString());
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if(couchbaseContainer!=null && couchbaseContainer.isRunning()){
            //couchbaseContainer.stop();
        }
    }
}
