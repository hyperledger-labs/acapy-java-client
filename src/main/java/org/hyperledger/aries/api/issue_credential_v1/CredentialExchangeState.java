/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public enum CredentialExchangeState {
    @JsonProperty("proposal_sent")
    @SerializedName("proposal_sent")
    PROPOSAL_SENT,

    @JsonProperty("proposal_received")
    @SerializedName("proposal_received")
    PROPOSAL_RECEIVED,

    @JsonProperty("offer_sent")
    @SerializedName("offer_sent")
    OFFER_SENT,

    @JsonProperty("offer_received")
    @SerializedName("offer_received")
    OFFER_RECEIVED,

    @JsonProperty("request_sent")
    @SerializedName("request_sent")
    REQUEST_SENT,

    @JsonProperty("request_received")
    @SerializedName("request_received")
    REQUEST_RECEIVED,

    @JsonProperty("credential_issued")
    @SerializedName("credential_issued")
    CREDENTIAL_ISSUED,

    @JsonProperty("credential_received")
    @SerializedName("credential_received")
    CREDENTIAL_RECEIVED,

    @JsonProperty("credential_acked")
    @SerializedName("credential_acked")
    CREDENTIAL_ACKED
}
