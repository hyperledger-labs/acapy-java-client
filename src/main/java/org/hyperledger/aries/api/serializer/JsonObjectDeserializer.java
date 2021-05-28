/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;

import java.io.IOException;

public class JsonObjectDeserializer extends JsonDeserializer<JsonObject> {

    @Override
    public JsonObject deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String json = node.toString();
        return com.google.gson.JsonParser.parseString(json).getAsJsonObject();
    }
}
