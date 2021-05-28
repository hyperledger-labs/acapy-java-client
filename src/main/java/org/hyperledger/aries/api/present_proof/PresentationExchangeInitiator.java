/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public enum PresentationExchangeInitiator {
    @JsonProperty("self")
    @SerializedName("self")
    SELF,

    @JsonProperty("external")
    @SerializedName("external")
    EXTERNAL,
}
