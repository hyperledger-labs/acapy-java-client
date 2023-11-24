/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries;

import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class IntegrationTestBase {

    private final Logger log = LoggerFactory.getLogger(IntegrationTestBase.class);

    public static final String ARIES_VERSION = "ghcr.io/hyperledger/aries-cloudagent-python:py3.9-0.10.5";
    public static final Integer ARIES_INBOUND_PORT = 8030;
    public static final Integer ARIES_ADMIN_PORT = 8031;

    protected AriesClient ac;

    @Container
    protected GenericContainer<?> ariesContainer = new GenericContainer<>(ARIES_VERSION)
            .withExposedPorts(ARIES_ADMIN_PORT)
            .withCommand("start"
                    + " -it http 0.0.0.0 " + ARIES_INBOUND_PORT
                    + " -ot http --admin 0.0.0.0 " + ARIES_ADMIN_PORT
                    + " --admin-insecure-mode"
                    + " --log-level debug"
                    + " -e http://0.0.0.0"
                    + " --no-ledger"
                    + " --wallet-type askar"
                    + " --wallet-name testWallet"
                    + " --wallet-key testKey"
                    + " --auto-provision"
                    + " --plugin aries_cloudagent.messaging.jsonld")
            .waitingFor(Wait.defaultWaitStrategy())
            .withLogConsumer(new Slf4jLogConsumer(log))
            ;

    @BeforeEach
    void setup() {
        ac = AriesClient.builder()
                .url(getHTTPAdminUrl())
                .build();
    }

    public String getHTTPAdminUrl() {
        return "http://localhost:" + ariesContainer.getMappedPort(ARIES_ADMIN_PORT);
    }

    public String getWSAdminUrl() {
        return "ws://localhost:" + ariesContainer.getMappedPort(ARIES_ADMIN_PORT) + "/ws";
    }
}
