/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public enum ConnectionState {
    @JsonProperty("init")
    @SerializedName("init")
    INIT,

    @JsonProperty("invitation")
    @SerializedName("invitation")
    INVITATION,

    @JsonProperty("request")
    @SerializedName("request")
    REQUEST,

    @JsonProperty("response")
    @SerializedName("response")
    RESPONSE,

    @JsonProperty("active")
    @SerializedName("active")
    ACTIVE,

    @JsonProperty("error")
    @SerializedName("error")
    ERROR,

    @JsonProperty("completed")
    @SerializedName("completed")
    COMPLETED,

    @JsonProperty("abandoned")
    @SerializedName("abandoned")
    ABANDONED,

    @JsonProperty("start")
    @SerializedName("start")
    START,

    @Deprecated // since 0.6.0
    @JsonProperty("inactive")
    @SerializedName("inactive")
    INACTIVE
}
