/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.multitenancy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public enum WalletKeyDerivation {

    @JsonProperty("ARGON2I_MOD")
    @SerializedName("ARGON2I_MOD")
    ARGON2I_MOD,

    @JsonProperty("ARGON2I_INT")
    @SerializedName("ARGON2I_INT")
    ARGON2I_INT,

    @JsonProperty("RAW")
    @SerializedName("RAW")
    RAW;
}
