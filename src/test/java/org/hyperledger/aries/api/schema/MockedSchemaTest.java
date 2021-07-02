/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.schema;

import java.util.List;
import java.util.Optional;

import org.hyperledger.acy_py.generated.model.TxnOrSchemaSendResult;
import org.hyperledger.aries.MockedTestBase;
import org.hyperledger.aries.api.schema.SchemaSendResponse.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;

@Slf4j
class MockedSchemaTest extends MockedTestBase {

    @Test
    void testSendSchema() throws Exception {
        String json = loader.load("files/schemas/schemaSendResults.json");

        server.enqueue(new MockResponse().setBody(json));

        final Optional<TxnOrSchemaSendResult> res = ac.schemas(SchemaSendRequest
                .builder()
                .schemaName("prefs")
                .schemaVersion("1.0")
                .attributes(List.of("score"))
                .build());

        Assertions.assertTrue(res.isPresent());
        Assertions.assertNotNull(res.get().getSent());
        Assertions.assertTrue(res.get().getSent().getSchemaId().startsWith("M6Mbe3qx7vB4wpZF4sBRjt"));
        Assertions.assertTrue(res.get().getSent().getSchema().getId().startsWith("M6Mbe3qx7vB4wpZF4sBRjt"));
        log.debug(pretty.toJson(res.get()));
    }

    @Test
    void testGetSchemaById() throws Exception {
        String json = loader.load("files/schemas/schemaGetResults.json");

        server.enqueue(new MockResponse().setBody(json));

        final Optional<Schema> res = ac.schemasGetById("571");

        Assertions.assertTrue(res.isPresent());
        Assertions.assertTrue(res.get().getId().startsWith("M6Mbe3qx7vB4wpZF4sBRjt"));
        Assertions.assertEquals("bank_account", res.get().getName());
        Assertions.assertEquals(571, res.get().getSeqNo());
        log.debug(pretty.toJson(res.get()));
    }
    
    @Test
    void testGetAllSchemas() throws Exception {
    	JsonArray jsonArray = new JsonArray();
    	jsonArray.add("exampleSchemaId");
    	JsonObject jsonObject = new JsonObject();
    	jsonObject.add("schema_ids", jsonArray);
    	
        String json = jsonObject.toString();
        System.out.println(json);

        server.enqueue(new MockResponse().setBody(json));

        Optional<List<String>> res = ac.schemasCreated(SchemasCreatedFilter.builder().build());

        Assertions.assertTrue(res.isPresent());
        Assertions.assertEquals(1, res.get().size());
        
        log.debug(pretty.toJson(res.get()));
    }

}
