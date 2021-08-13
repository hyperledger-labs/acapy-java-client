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
import lombok.NonNull;
import org.hyperledger.acy_py.generated.model.V20CredExRecord;

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

    // V1 sate only
    @JsonProperty("credential_acked")
    @SerializedName("credential_acked")
    CREDENTIAL_ACKED,

    // V2 state only
    @JsonProperty("done")
    @SerializedName("done")
    DONE
    ;

    /**
     * Maps v2 credential exchange states to their respective v1 states
     * @param v2 {@link V20CredExRecord.StateEnum}
     * @return {@link CredentialExchangeState}
     */
    public static CredentialExchangeState fromV2(@NonNull V20CredExRecord.StateEnum v2) {
        switch(v2) {
            case PROPOSAL_SENT:
                return PROPOSAL_SENT;
            case PROPOSAL_RECEIVED:
                return PROPOSAL_RECEIVED;
            case OFFER_SENT:
                return OFFER_SENT;
            case OFFER_RECEIVED:
                return OFFER_RECEIVED;
            case REQUEST_SENT:
                return REQUEST_SENT;
            case REQUEST_RECEIVED:
                return REQUEST_RECEIVED;
            case CREDENTIAL_ISSUED:
                return CREDENTIAL_ISSUED;
            case CREDENTIAL_RECEIVED:
                return CREDENTIAL_RECEIVED;
            case DONE:
                return DONE;
            default:
                return null;
        }
    }
}
