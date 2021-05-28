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

public enum PresentationExchangeState {
    @JsonProperty("proposal_sent")
    @SerializedName("proposal_sent")
    PROPOSAL_SENT,

    @JsonProperty("proposal_received")
    @SerializedName("proposal_received")
    PROPOSAL_RECEIVED,

    @JsonProperty("request_sent")
    @SerializedName("request_sent")
    REQUEST_SENT,

    @JsonProperty("request_received")
    @SerializedName("request_received")
    REQUEST_RECEIVED,

    @JsonProperty("presentation_sent")
    @SerializedName("presentation_sent")
    PRESENTATIONS_SENT,

    @JsonProperty("presentation_received")
    @SerializedName("presentation_received")
    PRESENTATION_RECEIVED,

    @JsonProperty("verified")
    @SerializedName("verified")
    VERIFIED,

    @JsonProperty("presentation_acked")
    @SerializedName("presentation_acked")
    PRESENTATION_ACKED
}
