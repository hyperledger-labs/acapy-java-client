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

public enum ConnectionTheirRole {
    @JsonProperty("invitee")
    @SerializedName("invitee")
    INVITEE,

    @JsonProperty("requester")
    @SerializedName("requester")
    REQUESTER,

    @JsonProperty("inviter")
    @SerializedName("inviter")
    INVITER,

    @JsonProperty("responder")
    @SerializedName("responder")
    RESPONDER
}
