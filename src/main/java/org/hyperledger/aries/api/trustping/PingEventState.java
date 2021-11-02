/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.trustping;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public enum PingEventState {

    /**
     * Agent received a ping event
     */
    @JsonProperty("received")
    @SerializedName(value = "received")
    RECEIVED,

    /**
     * Pinging agent received a response
     */
    @JsonProperty("response_received")
    @SerializedName(value = "response_received")
    RESPONSE_RECEIVED
}
