/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

import okhttp3.mockwebserver.MockResponse;
import org.hyperledger.aries.MockedTestBase;
import org.hyperledger.aries.config.GsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class MockedRevocationTest extends MockedTestBase {

    private final String response = loader.load("files/revocation/revRegCreateResponse.json");
    private final String parsed = loader.load("files/revocation/revRegCreateResponseJava.json");

    @Test
    void testCreateRevReg() throws Exception {
        server.enqueue(new MockResponse().setBody(response));

        final Optional<RevRegCreateResponse> reg = ac.revocationCreateRegistry(RevRegCreateRequest
                .builder()
                .credentialDefinitionId("VoSfM3eGaPxduty34ySygw:3:CL:571:sparta_bank")
                .build());

        Assertions.assertTrue(reg.isPresent());
        Assertions.assertEquals(parsed, GsonConfig.prettyPrinter().toJson(reg.get()));
    }

    @Test
    void testGetregistriesCreated() throws Exception {
        String reqCreated = loader.load("files/revocation/revRegsCreated.json");

        server.enqueue(new MockResponse().setBody(reqCreated));

        final Optional<RevRegsCreated> created = ac.revocationRegistriesCreated(null, null);

        Assertions.assertTrue(created.isPresent());
        Assertions.assertEquals(2, created.get().getRevRegIds().size());
        Assertions.assertTrue(created.get().getRevRegIds().get(0).startsWith("VoSfM3eGaPxduty34ySygw"));
    }

    @Test
    void testGetRevRegById() throws Exception {
        server.enqueue(new MockResponse().setBody(response));

        final Optional<RevRegCreateResponse> reg = ac.revocationRegistryGetById("mocked");

        Assertions.assertTrue(reg.isPresent());
        Assertions.assertEquals(parsed, GsonConfig.prettyPrinter().toJson(reg.get()));
    }

    @Test
    void testUpdateRegistryUri() throws Exception {
        server.enqueue(new MockResponse().setBody(response));

        final Optional<RevRegCreateResponse> reg = ac.revocationRegistryUpdateUri("mocked",
                new RevRegUpdateTailsFileUri("https://something.bar"));

        Assertions.assertTrue(reg.isPresent());
        Assertions.assertEquals(parsed, GsonConfig.prettyPrinter().toJson(reg.get()));
    }

    @Test
    void testGetActiveRegistry() throws Exception {
        String respActive = loader.load("files/revocation/revRegCreateResponseStateActive.json");
        String parsedActive = loader.load("files/revocation/revRegCreateResponseStateActiveJava.json");

        server.enqueue(new MockResponse().setBody(respActive));

        final Optional<RevRegCreateResponse> reg = ac.revocationActiveRegistry("mocked");

        Assertions.assertTrue(reg.isPresent());
        Assertions.assertEquals(parsedActive, GsonConfig.prettyPrinter().toJson(reg.get()));
    }

}
