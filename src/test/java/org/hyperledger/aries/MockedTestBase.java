/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries;

import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.Gson;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;

public abstract class MockedTestBase {

    protected FileLoader loader = FileLoader.newLoader();
    protected Gson gson = GsonConfig.defaultConfig();
    protected Gson pretty = GsonConfig.prettyPrinter();

    protected MockWebServer server;
    protected AriesClient ac;


    @BeforeEach
    void setup() throws Exception {
        server = new MockWebServer();
        server.start();
        final HttpUrl url = server.url("");
        ac = AriesClient
                .builder()
                .url("http://" + url.host() + ":" + url.port())
                .build();
    }

    @AfterEach
    void teardown() throws Exception {
        server.shutdown();
    }
}
