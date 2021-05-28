/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.server;

import org.hyperledger.aries.IntegrationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class AdminConfigTest extends IntegrationTestBase {

    @Test
    void testGetAdminConfig() throws Exception {
        Optional<AdminConfig> adminConfig = ac.statusConfig();
        Assertions.assertTrue(adminConfig.isPresent());

        // System.out.println(GsonConfig.prettyPrinter().toJson(adminConfig.get()));

        AdminConfig config = adminConfig.get();

        Optional<Boolean> mode = config.getAs("admin.admin_insecure_mode", Boolean.class);
        Assertions.assertTrue(mode.isPresent());
        Assertions.assertTrue(mode.get());

        Optional<String> port = config.getAs("admin.port", String.class);
        Assertions.assertTrue(port.isPresent());
        Assertions.assertEquals("8031", port.get());

        Optional<Number> messageSize = config.getAs("transport.max_message_size", Number.class);
        Assertions.assertTrue(messageSize.isPresent());
        Assertions.assertEquals(2097152, messageSize.get().longValue());

        Optional<List<String>> plugins = config.getAs("external_plugins", AdminConfig.COLLECTION_TYPE);
        Assertions.assertTrue(plugins.isPresent());
        Assertions.assertEquals("aries_cloudagent.messaging.jsonld", plugins.get().get(0));
    }
}
