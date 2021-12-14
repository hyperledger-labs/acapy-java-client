/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
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
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class MultiIntegrationTestBase {

    private final Logger log = LoggerFactory.getLogger(MultiIntegrationTestBase.class);

    public static final String ARIES_VERSION = "bcgovimages/aries-cloudagent:py36-1.16-1_0.7.2";
    public static final Integer ARIES_ENDPOINT_PORT = 8030;
    public static final Integer ARIES_ADMIN_PORT = 8031;
    public static final Integer ARIES_ENDPOINT_PORT_2 = 8040;
    public static final Integer ARIES_ADMIN_PORT_2 = 8041;

    protected AriesClient ac;
    protected AriesClient ac2;

    Network network = Network.newNetwork();

    @Container
    protected GenericContainer<?> ariesContainer = new GenericContainer<>(ARIES_VERSION)
            .withNetwork(network)
            .withNetworkAliases("agent_1")
            .withExposedPorts(ARIES_ADMIN_PORT)
            .withCommand("start"
                    + " -it http 0.0.0.0 " + ARIES_ENDPOINT_PORT
                    + " -ot http"
                    + " --admin 0.0.0.0 " + ARIES_ADMIN_PORT
                    + " --admin-insecure-mode"
                    + " --log-level debug"
                    + " -e http://agent_1:" + ARIES_ENDPOINT_PORT
                    + " --genesis-url https://indy-test.bosch-digital.de/genesis"
                    + " --auto-ping-connection"
                    + " --wallet-type indy"
                    + " --wallet-name agent1"
                    + " --wallet-key agent1key"
                    + " --auto-provision"
                    + " --plugin aries_cloudagent.messaging.jsonld")
            .waitingFor(Wait.defaultWaitStrategy())
            .withLogConsumer(new Slf4jLogConsumer(log))
            ;

    @Container
    protected GenericContainer<?> ariesContainer2 = new GenericContainer<>(ARIES_VERSION)
            .withExposedPorts(ARIES_ADMIN_PORT_2)
            .withNetwork(network)
            .withNetworkAliases("agent_2")
            .withCommand("start"
                    + " -it http 0.0.0.0 " + ARIES_ENDPOINT_PORT_2
                    + " -ot http"
                    + " --admin 0.0.0.0 " + ARIES_ADMIN_PORT_2
                    + " --admin-insecure-mode"
                    + " --log-level debug"
                    + " -e http://agent_2:" + ARIES_ENDPOINT_PORT_2
                    + " --genesis-url https://indy-test.bosch-digital.de/genesis"
                    + " --auto-ping-connection"
                    + " --wallet-type indy"
                    + " --wallet-name agent2"
                    + " --wallet-key agent2key"
                    + " --auto-provision"
                    + " --plugin aries_cloudagent.messaging.jsonld")
            .waitingFor(Wait.defaultWaitStrategy())
            .withLogConsumer(new Slf4jLogConsumer(log))
            ;

    @BeforeEach
    void setup() {
        ac = AriesClient.builder()
                .url("http://" + ariesContainer.getHost() + ":" + ariesContainer.getMappedPort(ARIES_ADMIN_PORT))
                .build();
        ac2 = AriesClient.builder()
                .url("http://" + ariesContainer.getHost() + ":" + ariesContainer2.getMappedPort(ARIES_ADMIN_PORT_2))
                .build();
    }
}
