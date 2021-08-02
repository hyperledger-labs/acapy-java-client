/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.IOException;

public class JsonArraySerializer extends JsonSerializer<JsonArray> {

    @Override
    public void serialize(JsonArray ja, JsonGenerator gen, SerializerProvider serializerProvider)
            throws IOException {
        gen.writeStartArray();
        if (ja != null && ja.size() > 0) {
            for (JsonElement jsonElement : ja) {
                gen.writeRawValue(jsonElement.toString());
            }
        }
        gen.writeEndArray();
    }
}
