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

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RevocationRegistryState {
    @JsonProperty("init")
    @SerializedName("init")
    INIT,

    @JsonProperty("generated")
    @SerializedName("generated")
    GENERATED,

    @JsonProperty("published")
    @SerializedName("published")
    PUBLISHED,

    @JsonProperty("staged")
    @SerializedName("staged")
    STAGED,

    @JsonProperty("active")
    @SerializedName("active")
    ACTIVE,

    @JsonProperty("full")
    @SerializedName("full")
    FULL
}
