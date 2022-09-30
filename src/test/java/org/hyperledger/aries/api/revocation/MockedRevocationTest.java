/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

import okhttp3.mockwebserver.MockResponse;
import org.hyperledger.acy_py.generated.model.IssuerRevRegRecord;
import org.hyperledger.aries.MockedTestBase;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MockedRevocationTest extends MockedTestBase {

    private final String response = FileLoader.load("files/revocation/revRegCreateResponse.json");
    private final String parsed = FileLoader.load("files/revocation/revRegCreateResponseJava.json");

    @Test
    void testCreateRevReg() throws Exception {
        server.enqueue(new MockResponse().setBody(response));

        IssuerRevRegRecord reg = ac.revocationCreateRegistry(RevRegCreateRequest
                .builder()
                .credentialDefinitionId("VoSfM3eGaPxduty34ySygw:3:CL:571:sparta_bank")
                .build())
                .orElseThrow();

        Assertions.assertEquals(parsed, GsonConfig.prettyPrinter().toJson(reg));
    }

    @Test
    void testGetregistriesCreated() throws Exception {
        String reqCreated = FileLoader.load("files/revocation/revRegsCreated.json");

        server.enqueue(new MockResponse().setBody(reqCreated));

        RevRegsCreated created = ac.revocationRegistriesCreated(null, null).orElseThrow();

        Assertions.assertEquals(2, created.getRevRegIds().size());
        Assertions.assertTrue(created.getRevRegIds().get(0).startsWith("VoSfM3eGaPxduty34ySygw"));
    }

    @Test
    void testGetRevRegById() throws Exception {
        server.enqueue(new MockResponse().setBody(response));

        IssuerRevRegRecord reg = ac.revocationRegistryGetById("mocked").orElseThrow();

        Assertions.assertEquals(parsed, GsonConfig.prettyPrinter().toJson(reg));
    }

    @Test
    void testUpdateRegistryUri() throws Exception {
        server.enqueue(new MockResponse().setBody(response));

        IssuerRevRegRecord reg = ac.revocationRegistryUpdateUri("mocked",
                new RevRegUpdateTailsFileUri("https://something.bar")).orElseThrow();

        Assertions.assertEquals(parsed, GsonConfig.prettyPrinter().toJson(reg));
    }

    @Test
    void testGetActiveRegistry() throws Exception {
        String respActive = FileLoader.load("files/revocation/revRegCreateResponseStateActive.json");
        String parsedActive = FileLoader.load("files/revocation/revRegCreateResponseStateActiveJava.json");

        server.enqueue(new MockResponse().setBody(respActive));

        IssuerRevRegRecord reg = ac.revocationActiveRegistry("mocked").orElseThrow();

        Assertions.assertEquals(parsedActive, GsonConfig.prettyPrinter().toJson(reg));
    }

}
