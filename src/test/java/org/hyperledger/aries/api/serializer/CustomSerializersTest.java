/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.present_proof.PresentProofRequest;
import org.hyperledger.aries.api.present_proof.PresentProofRequestHelper;
import org.hyperledger.aries.config.GsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CustomSerializersTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Gson gson = GsonConfig.defaultConfig();

    @Test
    void testProofSerializeDeserializeWithJackson() throws Exception {
        PresentProofRequest presentProofRequest = PresentProofRequestHelper
                .buildForAllAttributes("dummy", List.of("attr1", "attr2"), List.of(PresentProofRequest.
                        ProofRequest.ProofAttributes.ProofRestrictions
                        .builder()
                        .issuerDid("did:sov:123")
                        .build()));
        String serialized = mapper.writeValueAsString(presentProofRequest);
        PresentProofRequest deserialized = mapper.readValue(serialized, PresentProofRequest.class);
        Assertions.assertEquals(presentProofRequest, deserialized);
    }

    @Test
    void testProofSerializeDeserializeWithGson() {
        PresentProofRequest presentProofRequest = PresentProofRequestHelper
                .buildForAllAttributes("dummy", List.of("attr1", "attr2"), List.of(PresentProofRequest.
                        ProofRequest.ProofAttributes.ProofRestrictions
                        .builder()
                        .issuerDid("did:sov:123")
                        .build()));
        String serialized = gson.toJson(presentProofRequest);
        PresentProofRequest deserialized = gson.fromJson(serialized, PresentProofRequest.class);
        Assertions.assertEquals(presentProofRequest, deserialized);
    }

    @Test
    void testSimplePojoJackson() throws Exception {
        String json = "{\"name\": \"Proof request\"}";
        JsonObject jo = gson.fromJson(json, JsonObject.class);
        DummyPojo pojo = new DummyPojo(jo);
        String serialized = mapper.writeValueAsString(pojo);
        DummyPojo deserialized = mapper.readValue(serialized, DummyPojo.class);
        Assertions.assertEquals(pojo, deserialized);
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    private static final class DummyPojo {
        @JsonDeserialize(using = JsonObjectDeserializer.class)
        @JsonSerialize(using = JsonObjectSerializer.class)
        private JsonObject value;
    }
}
