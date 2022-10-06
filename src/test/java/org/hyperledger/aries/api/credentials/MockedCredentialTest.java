/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.credentials;

import okhttp3.mockwebserver.MockResponse;
import org.hyperledger.acy_py.generated.model.CredentialDefinitionGetResult;
import org.hyperledger.aries.MockedTestBase;
import org.hyperledger.aries.api.credential_definition.CredentialDefinition;
import org.hyperledger.aries.api.credential_definition.CredentialDefinition.CredentialDefinitionRequest;
import org.hyperledger.aries.api.issue_credential_v1.CredentialExchangeState;
import org.hyperledger.aries.api.issue_credential_v1.IssueCredentialRecordsFilter;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialExchange;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

class MockedCredentialTest extends MockedTestBase {

    private final String schemaId = "CHysca6fY8n8ytCDLAJGZj:2:certificate:1.0";
    private final String credentialDefinitionId = "RupFs4ns7UmyGvWKizU56o:3:CL:133:ISO9001";

    @Test
    void testCreateCredentialDefinition() throws Exception {
        server.enqueue(new MockResponse().setBody("\n" +
                "{\n" +
                "  \"credential_definition_id\": \"F6dB7dMVHUQSC64qemnBi7:3:CL:571:my-local-test-bank-foo\"\n" +
                "}"));

        final Optional<CredentialDefinition.CredentialDefinitionResponse> c = ac.credentialDefinitionsCreate(
                new CredentialDefinitionRequest());
        Assertions.assertTrue(c.isPresent());
        Assertions.assertTrue(c.get().getCredentialDefinitionId().startsWith("F6dB7dMVHUQSC64qemnBi7"));
    }

    @Test
    void testGetCredentialDefinition() throws Exception {
        String json = FileLoader.load("files/credentialDefinition.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<CredentialDefinitionGetResult> cd = ac.credentialDefinitionsGetById("mocked");
        Assertions.assertTrue(cd.isPresent());
        Assertions.assertEquals("108", cd.get().getCredentialDefinition().getSchemaId());
    }

    @Test
    void testGetCredential() throws Exception {
        String json = FileLoader.load("files/credential.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<Credential> credential = ac.credential("mock");
        Assertions.assertTrue(credential.isPresent());
        Assertions.assertTrue(credential.get().getReferent().startsWith("db439a72"));
    }

    @Test
    void testGetAllCredentials() throws Exception {
        String json = FileLoader.load("files/credentials.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<List<Credential>> credentials = ac.credentials();
        Assertions.assertTrue(credentials.isPresent());
        Assertions.assertEquals(5, credentials.get().size());
    }

    @Test
    void testGetCredentialsBySchemaId() throws Exception {
        String json = FileLoader.load("files/credentials.json");
        server.enqueue(new MockResponse().setBody(json));

        Optional<List<Credential>> credentials = ac.credentials(CredentialFilter.schemaId(schemaId));
        Assertions.assertTrue(credentials.isPresent());
        Assertions.assertEquals(4, credentials.get().size());

        server.enqueue(new MockResponse().setBody(json));
        credentials = ac.credentials(CredentialFilter.schemaId(schemaId).negate());
        Assertions.assertTrue(credentials.isPresent());
        Assertions.assertEquals(1, credentials.get().size());
    }

    @Test
    void testGetCredentialsByCredentialDefinitionId() throws Exception {
        String json = FileLoader.load("files/credentials.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<List<Credential>> credentials = ac
                .credentials(CredentialFilter.credentialDefinitionId(credentialDefinitionId));
        Assertions.assertTrue(credentials.isPresent());
        Assertions.assertEquals(1, credentials.get().size());
    }

    @Test
    void testGetCredentialsByCredentialDefinitionIdAndSchemaId() throws Exception {
        String json = FileLoader.load("files/credentials.json");
        server.enqueue(new MockResponse().setBody(json));

        final List<String> credentials = ac.credentialIds(CredentialFilter
                .credentialDefinitionId(credentialDefinitionId).and(CredentialFilter.schemaId(schemaId)));
        Assertions.assertEquals(1, credentials.size());
        Assertions.assertEquals("60591077-717b-429b-bda1-f5930d2870c7", credentials.get(0));
    }

    @Test
    void testGetIssueCredentialRecords() throws Exception {
        String json = FileLoader.load("files/issueCredentialRecords");
        server.enqueue(new MockResponse().setBody(json));

        Optional<List<V1CredentialExchange>> exchanges = ac.issueCredentialRecords(
                IssueCredentialRecordsFilter.builder().state(CredentialExchangeState.PROPOSAL_RECEIVED).build());
        Assertions.assertTrue(exchanges.isPresent());
        Assertions.assertEquals(2, exchanges.get().size());
        Assertions.assertEquals(CredentialExchangeState.PROPOSAL_RECEIVED, exchanges.get().get(0).getState());
    }

    @Test
    void testGetCredentialMimeTypes() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n" +
                "  \"results\": {\n" +
                "    \"bic\": \"application/json\"\n" +
                "  }\n" +
                "}"));
        Map<String, String> c = ac.credentialMimeTypes("referent").orElseThrow();
        Assertions.assertEquals("application/json", c.get("bic"));
    }

}
