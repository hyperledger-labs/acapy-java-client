/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.ledger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public enum IndyLedgerRoles {

    @JsonProperty("steward")
    @SerializedName("steward")
    STEWARD,

    @JsonProperty("trustee")
    @SerializedName("trustee")
    TRUSTEE,

    @JsonProperty("endorser")
    @SerializedName("endorser")
    ENDORSER,

    @JsonProperty("network_monitor")
    @SerializedName("network_monitor")
    NETWORK_MONITOR
}
