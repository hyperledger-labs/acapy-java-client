/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.multitenancy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * Wallet type
 */
public enum WalletType {
    @JsonProperty("askar")
    @SerializedName("askar")
    ASKAR,

    @JsonProperty("in_memory")
    @SerializedName("in_memory")
    IN_MEMORY,

    @JsonProperty("indy")
    @SerializedName("indy")
    INDY
}
