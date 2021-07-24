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

public enum TransactionState {
    @JsonProperty("transaction_created")
    @SerializedName("transaction_created")
    TRANSACTION_CREATED,

    @JsonProperty("request_sent")
    @SerializedName("request_sent")
    REQUEST_SENT,

    @JsonProperty("request_received")
    @SerializedName("request_received")
    REQUEST_RECEIVED,

    @JsonProperty("transaction_endorsed")
    @SerializedName("transaction_endorsed")
    TRANSACTION_ENDORSED,

    @JsonProperty("transaction_refused")
    @SerializedName("transaction_refused")
    TRANSACTION_REFUSED,

    @JsonProperty("transaction_resent")
    @SerializedName("transaction_resent")
    TRANSACTION_RESENT,

    @JsonProperty("transaction_resent_received")
    @SerializedName("transaction_resent_received")
    TRANSACTION_RESENT_RECEIEVED,

    @JsonProperty("transaction_cancelled")
    @SerializedName("transaction_cancelled")
    TRANSACTION_CANCELLED,

    @JsonProperty("transaction_acked")
    @SerializedName("transaction_acked")
    TRANSACTION_ACKED

}
