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
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

public class JsonObjectArraySerializer extends JsonSerializer<List<JsonObject>> {

    @Override
    public void serialize(List<JsonObject> jo, JsonGenerator gen,
                          SerializerProvider serializerProvider) throws IOException {
        gen.writeStartArray();
        if (jo != null && jo.size() > 0) {
            for(JsonObject o : jo) {
                gen.writeRawValue(o.toString());
            }
        }
        gen.writeEndArray();
    }
}
