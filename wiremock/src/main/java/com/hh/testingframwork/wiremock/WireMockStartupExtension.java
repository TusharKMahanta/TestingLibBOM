package com.hh.testingframwork.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class WireMockStartupExtension implements BeforeAllCallback, AfterAllCallback {
    public static WireMockServer wireMockServer;

    public WireMockStartupExtension(){
        wireMockServer =
                new WireMockServer(
                        WireMockConfiguration.wireMockConfig()
                                .port(WireMockConfiguration.DYNAMIC_PORT)
                                .usingFilesUnderClasspath("stubs")
                                .notifier(new ConsoleNotifier(false)));
    }

    public WireMockStartupExtension(String stubFolderName){
        wireMockServer =
                new WireMockServer(
                        WireMockConfiguration.wireMockConfig()
                                .port(WireMockConfiguration.DYNAMIC_PORT)
                                .usingFilesUnderClasspath(stubFolderName)
                                .notifier(new ConsoleNotifier(false)));
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        wireMockServer.start();
        log.info("Set wiremock url fps.testcontainer.wiremock.url : ", wireMockServer.baseUrl());
        //System.setProperty("fps.testcontainer.wiremock.url", wireMockServer.baseUrl()Integer.toString(wireMockServer.port()));
        System.setProperty("fps.testcontainer.wiremock.url", wireMockServer.baseUrl());
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if(wireMockServer!=null && wireMockServer.isRunning()){
            //wireMockServer.stop();
        }
    }
}
