/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonConfig {

    /**
     * Builder based on aca-py's default field naming strategy
     * @return {@link Gson}
     */
    public static Gson defaultConfig() {
        return defaultBuilder()
                .create()
                ;
    }

    public static Gson prettyPrinter() {
        return defaultBuilder()
                .setPrettyPrinting()
                .create()
                ;
    }

    /**
     * Matches jackson's default serialization/deserialization behaviour
     * @return {@link Gson}
     */
    public static Gson jacksonBehaviour() {
        return new GsonBuilder()
                .serializeNulls()
                .create()
                ;
    }

    /**
     * Builder based on aca-py's default field naming strategy
     * but without HTML escaping
     * @return {@link Gson}
     */
    public static Gson defaultNoEscaping() {
        return defaultBuilder()
                .disableHtmlEscaping()
                .create()
                ;
    }

    private static GsonBuilder defaultBuilder() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                ;
    }
}
