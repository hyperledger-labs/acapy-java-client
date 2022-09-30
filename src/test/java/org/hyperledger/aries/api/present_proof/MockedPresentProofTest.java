/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import org.hyperledger.aries.MockedTestBase;
import org.hyperledger.aries.api.serializer.JsonObjectSerializer;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class MockedPresentProofTest extends MockedTestBase {

    @Test
    void testParsePresentationResponse() {
        String json = FileLoader.load("files/present-proof-request-response.json");
        PresentationExchangeRecord response = gson.fromJson(json, PresentationExchangeRecord.class);
        assertEquals("23243302324860431744596330413752559589", response.getPresentationRequest().getNonce());
    }

    @Test
    void testGetPresentationExchangeRecords() throws Exception {
        String json = FileLoader.load("files/present-proof-records.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<List<PresentationExchangeRecord>> res = ac.presentProofRecords();

        Assertions.assertTrue(res.isPresent());
        assertEquals(2, res.get().size());
        Assertions.assertTrue(res.get().get(1).getConnectionId().startsWith("d6cf95bd"));
    }

    @Test
    void testGetPresentationExchangeRecord() throws Exception {
        String json = FileLoader.load("files/present-proof-record.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<PresentationExchangeRecord> res = ac.presentProofRecordsGetById("mock");

        Assertions.assertTrue(res.isPresent());
        Assertions.assertTrue(res.get().getConnectionId().startsWith("00598f57"));
        log.debug(pretty.toJson(res.get()));
    }

    @Test
    void testGetPresentProofRecordsCredentials() throws Exception {
        String json = FileLoader.load("files/present-proof-records-credentials.json");
        server.enqueue(new MockResponse().setBody(json));

        Optional<List<PresentationRequestCredentials>> credentials = ac
                .presentProofRecordsCredentials("mock");

        Assertions.assertTrue(credentials.isPresent());
        Assertions.assertEquals(2, credentials.get().size());
        Assertions.assertEquals("bpa", credentials.get().get(0).getCredentialInfo().getAttrs().get("name"));
    }

    @Test
    void testGetPresentProofRecordsCredentialsFilter() {
        PresentationRequestCredentialsFilter filter = PresentationRequestCredentialsFilter
                .builder()
                .referent(List.of("foo", "bar"))
                .build();
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse("http://localhost/foo")).newBuilder();
        filter.buildParams(builder);
        Assertions.assertTrue(builder.build().toString().endsWith("referent=foo%2Cbar"));
    }

    @Test
    void testJacksonSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(JsonObject.class, new JsonObjectSerializer());
        mapper.registerModule(module);

        String json = FileLoader.load("files/present-proof-records.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<List<PresentationExchangeRecord>> res = ac.presentProofRecords();

        Assertions.assertTrue(res.isPresent());
        assertEquals(2, res.get().size());

        assertEquals(
                mapper.writeValueAsString(res.get().get(0).getPresentationProposalDict()),
                GsonConfig.jacksonBehaviour().toJson(res.get().get(0).getPresentationProposalDict()));

        assertEquals(
                mapper.writeValueAsString(res.get().get(1).getPresentation()),
                GsonConfig.jacksonBehaviour().toJson(res.get().get(1).getPresentation()));
    }
}
