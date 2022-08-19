/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.server;

import org.hyperledger.aries.IntegrationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminConfigTest extends IntegrationTestBase {

    @Test
    void testGetAdminConfigWrapped() throws Exception {
        Optional<AdminConfig> adminConfig = ac.statusConfig();
        Assertions.assertTrue(adminConfig.isPresent());

        // System.out.println(GsonConfig.prettyPrinter().toJson(adminConfig.get()));

        Optional<String> p = adminConfig.flatMap(c -> c.getAs("admin.port", String.class));
        Assertions.assertTrue(p.isPresent());
        Assertions.assertEquals("8031", p.get());

        AdminConfig config = adminConfig.get();
        Optional<Boolean> mode = config.getAs("admin.admin_insecure_mode", Boolean.class);
        Assertions.assertTrue(mode.isPresent());
        Assertions.assertTrue(mode.get());

        Optional<Boolean> empty = config.getAs("something", Boolean.class);
        Assertions.assertFalse(empty.isPresent());

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

    @Test
    void testGetAdminConfigUnwrapped() throws Exception {
        Optional<AdminConfig> adminConfig = ac.statusConfig();
        Assertions.assertTrue(adminConfig.isPresent());

        AdminConfig config = adminConfig.get();
        Boolean mode = config.getUnwrapped("admin.admin_insecure_mode", Boolean.class);
        Assertions.assertNotNull(mode);
        Assertions.assertTrue(mode);

        Boolean empty = config.getUnwrapped("something", Boolean.class);
        Assertions.assertNull(empty);

        Boolean conversionFailure = config.getUnwrapped("transport.max_message_size", Boolean.class);
        Assertions.assertNull(conversionFailure);

        String port = config.getUnwrapped("admin.port", String.class);
        Assertions.assertNotNull(port);
        Assertions.assertEquals("8031", port);

        Number messageSize = config.getUnwrapped("transport.max_message_size", Number.class);
        Assertions.assertNotNull(messageSize);
        Assertions.assertEquals(2097152, messageSize.longValue());

        List<String> plugins = config.getUnwrapped("external_plugins", AdminConfig.COLLECTION_TYPE);
        Assertions.assertNotNull(plugins);
        Assertions.assertEquals("aries_cloudagent.messaging.jsonld", plugins.get(0));
    }

    @Test
    void testGetTypedServerStatusConfig() throws IOException {
        StatusConfig config = ac.statusConfigTyped().orElseThrow();
        Assertions.assertTrue(config.getAdminInsecureMode());
        Assertions.assertEquals("8031", config.getAdminPort());
        Assertions.assertEquals(2097152, config.getTransportMaxMessageSize());
        Assertions.assertEquals("aries_cloudagent.messaging.jsonld", config.getExternalPlugins().get(0));
    }

    @Test
    void testLoadPlugins() throws IOException {
        List<String> plugins = ac.plugins().orElseThrow();
        Assertions.assertTrue(plugins.size() > 0);
    }
}
