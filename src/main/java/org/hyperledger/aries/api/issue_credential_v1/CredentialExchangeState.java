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

/**
 * Manual credential exchange via aca-py rest api:
 * 1. Holder: /issue-credential/send-proposal
 * 2. Issuer: /issue-credential/records/{id}/send-offer
 * 3. Holder: /issue-credential/records/{id}/send-request
 * 4. Issuer: /issue-credential/records/{id}/send
 * 5. Holder:/issue-credential/records/{id}/store
 */
public enum CredentialExchangeState {

    /**
     * Step 1: Holder sends proposal to issuer
     */
    @JsonProperty("proposal_sent")
    @SerializedName("proposal_sent")
    PROPOSAL_SENT,

    /**
     * Step 1: Issuer receives credential proposal from holder
     */
    @JsonProperty("proposal_received")
    @SerializedName("proposal_received")
    PROPOSAL_RECEIVED,

    /**
     * Step 2: Issuer sends counter offer to holder, issuer can also
     * start the process by sending an offer without receiving a proposal first
     */
    @JsonProperty("offer_sent")
    @SerializedName("offer_sent")
    OFFER_SENT,

    /**
     * Step 2: Holder receives (counter) offer from issuer
     */
    @JsonProperty("offer_received")
    @SerializedName("offer_received")
    OFFER_RECEIVED,

    /**
     * Step 3: Holder requests credential from issuer
     */
    @JsonProperty("request_sent")
    @SerializedName("request_sent")
    REQUEST_SENT,

    /**
     * Step 3: Issuer receives credential request from holder
     */
    @JsonProperty("request_received")
    @SerializedName("request_received")
    REQUEST_RECEIVED,

    /**
     * Step 4: Issuer sends credential to holder
     */
    @JsonProperty("credential_issued")
    @SerializedName("credential_issued")
    CREDENTIAL_ISSUED,

    /**
     * Step 4: Holder receives credential from issuer
     */
    @JsonProperty("credential_received")
    @SerializedName("credential_received")
    CREDENTIAL_RECEIVED,

    /**
     * V1 state only
     * Step5: Credential exchange completed
     */
    @JsonProperty("credential_acked")
    @SerializedName("credential_acked")
    CREDENTIAL_ACKED,

    /**
     * V2 state only
     * Step 5: Credential exchange completed
     */
    @JsonProperty("done")
    @SerializedName("done")
    DONE,

    /**
     * Not an aries state, but useful in business logic reacting on these states
     */
    @JsonProperty("revoked")
    @SerializedName("revoked")
    REVOKED
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
                throw new IllegalStateException("V2 state could not be converted to its V1 counterpart");
        }
    }
}
