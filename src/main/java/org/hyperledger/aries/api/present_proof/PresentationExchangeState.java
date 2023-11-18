/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * ARIES presentation exchange states for V1 and V2.
 */
public enum PresentationExchangeState {
    @JsonProperty("proposal_sent")
    @SerializedName(value = "proposal_sent", alternate = "proposal-sent")
    PROPOSAL_SENT,

    @JsonProperty("proposal_received")
    @SerializedName(value = "proposal_received", alternate = "proposal-received")
    PROPOSAL_RECEIVED,

    @JsonProperty("request_sent")
    @SerializedName(value = "request_sent", alternate = "request-sent")
    REQUEST_SENT,

    @JsonProperty("request_received")
    @SerializedName(value = "request_received", alternate = "request-received")
    REQUEST_RECEIVED,

    @JsonProperty("presentation_sent")
    @SerializedName(value = "presentation_sent", alternate = "presentation-sent")
    PRESENTATIONS_SENT,

    @JsonProperty("presentation_received")
    @SerializedName(value = "presentation_received", alternate = "presentation-received")
    PRESENTATION_RECEIVED,

    /** V1 state only */
    @JsonProperty("verified")
    @SerializedName("verified")
    VERIFIED,

    /** V1 state only */
    @JsonProperty("presentation_acked")
    @SerializedName("presentation_acked")
    PRESENTATION_ACKED,

    /** V2 state only */
    @JsonProperty("done")
    @SerializedName("done")
    DONE,

    /** V2 state only */
    @JsonProperty("abandoned")
    @SerializedName("abandoned")
    ABANDONED,

    /** V2 state only */
    @JsonProperty("deleted")
    @SerializedName("deleted")
    DELETED,

    /** Not an aries state, can be used in a manual proof exchange to mark the state
     * as declined if the request was not accepted */
    @JsonProperty("declined")
    @SerializedName("declined")
    DECLINED
}
