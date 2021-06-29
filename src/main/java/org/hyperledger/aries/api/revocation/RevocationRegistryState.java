/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor @Getter
public enum RevocationRegistryState {
    @JsonProperty("init")
    @SerializedName("init")
    INIT("init"),

    @JsonProperty("generated")
    @SerializedName("generated")
    GENERATED("generated"),

    @JsonProperty("published")
    @SerializedName("published")
    PUBLISHED("published"),

    @JsonProperty("staged")
    @SerializedName("staged")
    STAGED("staged"),

    @JsonProperty("active")
    @SerializedName("active")
    ACTIVE("active"),

    @JsonProperty("full")
    @SerializedName("full")
    FULL("full")
    ;

    private final String value;

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
