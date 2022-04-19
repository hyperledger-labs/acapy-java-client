/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.ledger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public enum IndyLedgerRoles {

    @JsonProperty("STEWARD")
    @SerializedName("STEWARD")
    STEWARD,

    @JsonProperty("TRUSTEE")
    @SerializedName("TRUSTEE")
    TRUSTEE,

    @JsonProperty("ENDORSER")
    @SerializedName("ENDORSER")
    ENDORSER,

    @JsonProperty("NETWORK_MONITOR")
    @SerializedName("NETWORK_MONITOR")
    NETWORK_MONITOR,

    @JsonProperty("reset")
    @SerializedName("reset")
    RESET
}
