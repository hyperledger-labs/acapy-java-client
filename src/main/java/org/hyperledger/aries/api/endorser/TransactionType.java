/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.endorser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public enum TransactionType {
    @JsonProperty("1")
    @SerializedName("1")
    NYM,

    @JsonProperty("100")
    @SerializedName("100")
    ATTRIB,

    @JsonProperty("101")
    @SerializedName("101")
    SCHEMA,

    @JsonProperty("102")
    @SerializedName("102")
    CREDENTIIAL_DEFINITION,

    @JsonProperty("113")
    @SerializedName("113")
    REVOC_REG_DEF,

    @JsonProperty("114")
    @SerializedName("114")
    REVOC_REV_ID

}
