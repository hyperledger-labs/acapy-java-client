/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord.Identifier;
import org.hyperledger.aries.config.GsonConfig;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
public class EventParser {

    private static final Type IDENTIFIER_TYPE = new TypeToken<Collection<Identifier>>(){}.getType();

    private static final Gson gson = GsonConfig.defaultConfig();
    private final Gson pretty = GsonConfig.prettyPrinter();

    public String prettyJson(@NonNull String json) {
        JsonElement el = JsonParser.parseString(json);
        return pretty.toJson(el);
    }

    public <T> Optional<T> parseValueSave(@NonNull String json, @NonNull Class<T> valueType) {
        Optional<T> t = Optional.empty();
        try {
            t = Optional.ofNullable(gson.fromJson(json, valueType));
        } catch (JsonSyntaxException e) {
            log.error("Could not format json body", e);
        }
        return t;
    }

    public static <T> Optional<T> parseElement(JsonElement json, @NonNull Class<T> valueType) {
        return Optional.ofNullable(gson.fromJson(json, valueType));
    }

    public static <T> Optional<T> parseElement(JsonElement json, @NonNull Type valueType) {
        return Optional.ofNullable(gson.fromJson(json, valueType));
    }

    public <T> void parseValueSave(@NonNull String json, @NonNull Class<T> valueType, Consumer<T> consumer) {
        parseValueSave(json, valueType).ifPresent(consumer);
    }

    public Optional<PresentationExchangeRecord> parsePresentProof(String json) {
        Optional<PresentationExchangeRecord> presentation = parseValueSave(json, PresentationExchangeRecord.class);
        if (presentation.isPresent()) {
            List<Identifier> identifiers = resolveIdentifiers(presentation.get().getPresentation());
            presentation.get().setIdentifiers(identifiers);
        }
        return presentation;
    }

    public static List<Identifier> resolveIdentifiers(JsonObject jo) {
        if (jo != null) {
            JsonElement je = jo.get("identifiers");
            return gson.fromJson(je, IDENTIFIER_TYPE);
        }
        return List.of();
    }
}
