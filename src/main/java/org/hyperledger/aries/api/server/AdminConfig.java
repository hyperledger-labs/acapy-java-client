/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.config.GsonConfig;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Low level admin config with value extractor
 */
@Slf4j
@Data @NoArgsConstructor
public class AdminConfig {

    private static final Gson gson = GsonConfig.defaultConfig();
    public static final Type COLLECTION_TYPE = new TypeToken<Collection<String>>(){}.getType();

    private Map<String, JsonElement> config;

    /**
     * Get a config value
     * @param key the config key e.g. debug.auto_accept_invites
     * @param type the desired return type
     * @param <T> – the type of the desired result object
     * @return the value or an empty optional if the key was not found or conversion failed
     */
    public <T> Optional<T> getAs(@NonNull String key, @NonNull Type type) {
        JsonElement value = config.get(key);
        if (value != null) {
            try {
                return Optional.ofNullable(gson.fromJson(value, type));
            } catch (Exception e) {
                log.error("Could not convert {} to {}", key, type, e);
            }
        }
        return Optional.empty();
    }

    /**
     * Get a config value
     * @param key the config key e.g. debug.auto_accept_invites
     * @param type the desired return type
     * @param <T> – the type of the desired result object
     * @return the value or null if the key was not found or conversion failed
     */
    public <T> T getUnwrapped(@NonNull String key, @NonNull Type type) {
        JsonElement value = config.get(key);
        if (value != null) {
            try {
                return gson.fromJson(value, type);
            } catch (Exception e) {
                log.error("Could not convert {} to {}", key, type, e);
            }
        }
        return null;
    }
}
