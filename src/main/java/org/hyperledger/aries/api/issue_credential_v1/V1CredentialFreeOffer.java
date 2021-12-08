/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.acy_py.generated.model.AttachDecorator;
import org.hyperledger.aries.api.present_proof.ProofRequestPresentation;

import java.util.List;

/**
 * Model class for a connection-less credential offer. It is composed of two api requests:
 * 1. /issue-credential/create
 * 2. /connections/create-invitation
 * Like it is with the connection-less proof-request the barcode only points to a temporary URL which then
 * redirects to another URL in the location header that then
 * contains the credential offer in the d_m parameter like https://mydomain.com?d_m=ey...
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class V1CredentialFreeOffer {

    // references to the created records - not part of the offer

    /** internal mapping to the exchange record */
    @JsonIgnore
    private transient String credentialExchangeId;

    /** internal mapping to the thread id */
    @JsonIgnore
    private transient String threadId;

    /** internal mapping to the connection record */
    @JsonIgnore
    private transient String connectionId;

    // offer data

    private String comment;

    /** credential_offer_dict.type */
    @SerializedName("@type")
    private String type;

    /** credential_offer_dict.credential_preview */
    private V1CredentialExchange.CredentialProposalDict.CredentialProposal credentialPreview;

    /** credential_offer_dict.offers~attach */
    @SerializedName("offers~attach")
    private List<AttachDecorator> offersAttach;

    /**
     * Constructed off:
     * invitation.recipientKeys
     * invitation.routingKeys
     * invitation.serviceEndpoint
     */
    @SerializedName("~service")
    private ProofRequestPresentation.ServiceDecorator service;
}
